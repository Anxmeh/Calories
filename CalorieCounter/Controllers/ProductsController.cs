using CalorieCounter.Entities;
using CalorieCounter.Models;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;

namespace CalorieCounter.Controllers
{
    [Route("api/[controller]")]
    [ApiController]

    public class ProductsController : ControllerBase
    {
        private readonly EFContext _context;
        private readonly IWebHostEnvironment _env;
        public ProductsController(EFContext context, IWebHostEnvironment env)
        {
            _context = context;
            _env = env;
        }

         [HttpGet("products")]
        public IActionResult GetAllProducts()
        {
           

            var query = _context.Products.AsQueryable();

            ICollection<ProductViewModel> result;

            result = query.Select(p => new ProductViewModel
            {
                Id = p.Id,
                Name = p.Name,
                Protein = p.Protein,
                Fat = p.Fat,
                Carbohydrate = p.Carbohydrate,
                Calories = p.Calories,
            }).ToList();

            return Ok(result);
        }
        [HttpPost("addproduct")]
        public IActionResult AddProduct([FromBody] AddProductViewModel model)
        {
            var product = _context.Products.SingleOrDefault(p => p.Name == model.Name);
            if (product != null)
                return BadRequest(new { invalid = "Such a product is in the database" });
            product = new Product
            {
                Name = model.Name,
                Calories = model.Calories,
                Protein = model.Protein,
                Carbohydrate = model.Carbohydrate,
                Fat = model.Fat
            };
            _context.Products.Add(product);
            _context.SaveChanges();
            _context.ProductsInDish.RemoveRange(_context.ProductsInDish);
            _context.SaveChanges();

          
            return Ok(new ProductViewModel
            {
                Name = product.Name,
                Calories = product.Calories,
                Protein = product.Protein,
                Carbohydrate = product.Carbohydrate,
                Fat = product.Fat
            });
        }

        //[HttpPost("removeproduct")]
        //public IActionResult RemoveProduct([FromBody] RemoveProductViewModel model)
        //{
        //    var product = _context.Products.SingleOrDefault(p => p.Name == model.Name);
        //    if (product == null)
        //        return BadRequest(new { invalid = "Not found" });
            
        //    _context.Products.Remove(product);
        //    _context.SaveChanges();
        //    return Ok();
        //}

        [HttpPost("addproducttodish")]
        public IActionResult AddProductToDish([FromBody] AddDishViewModel model)
        {
            
            var product =  new Dish
            {
                ProductName = model.ProductName,
                ProductCalories = model.ProductCalories,
                ProductProtein = model.ProductProtein,
                ProductCarbohydrate = model.ProductCarbohydrate,
                ProductFat = model.ProductFat,
                ProductWeight = model.ProductWeight
            };
            _context.ProductsInDish.Add(product);
            _context.SaveChanges();

            return Ok(new DishViewModel
            {
                ProductName = product.ProductName,
                ProductCalories = product.ProductCalories,
                ProductProtein = product.ProductProtein,
                ProductCarbohydrate = product.ProductCarbohydrate,
                ProductFat = product.ProductFat,
                ProductWeight = product.ProductWeight
            });
        }
        [HttpPost("removeproduct")]
        public IActionResult RemoveProduct([FromBody]int productId)
        {
            var product = _context.Products.SingleOrDefault(p => p.Id == productId);
            if (product != null)
            {
                _context.Products.Remove(product);
                _context.SaveChanges();
            }
            else
                return BadRequest(new { invalid = "Не знайдено" });

            return Ok();
        }

        [HttpPost("removeproductindish")]
        public IActionResult RemoveProductInDish([FromBody] RemoveDishViewModel model)
        {
            var product = _context.ProductsInDish.FirstOrDefault(p => p.ProductName == model.ProductName);
            if (product == null)
                return BadRequest(new { invalid = "Not found" });

            _context.ProductsInDish.Remove(product);
            _context.SaveChanges();

            //////////
            var query = _context.ProductsInDish.AsQueryable();
            ICollection<DishViewModel> ingredients;

            ingredients = query.Select(d => new DishViewModel
            {
                Id = d.Id,
                ProductName = d.ProductName,
                ProductProtein = d.ProductProtein,
                ProductFat = d.ProductFat,
                ProductCarbohydrate = d.ProductCarbohydrate,
                ProductCalories = d.ProductCalories,
                ProductWeight = d.ProductWeight
            }).ToList();

            double calories = 0;
            double weight = 0;
            double proteins = 0;
            double carbs = 0;
            double fats = 0;

            foreach (var item in ingredients)
            {
                double cal = item.ProductCalories * item.ProductWeight / 100;
                double protein = item.ProductProtein * item.ProductWeight / 100;
                double carb = item.ProductCarbohydrate * item.ProductWeight / 100;
                double fat = item.ProductFat * item.ProductWeight / 100;
                calories += cal;
                proteins += protein;
                carbs += carb;
                fats += fat;
                weight += item.ProductWeight;
            }


            return Ok(new ResultDishViewModel
            {
                DishName = String.Empty,
                DishCalories = calories,
                DishWeight = weight,
                DishCarbohydrate = carbs,
                DishFat = fats,
                DishProtein = proteins,
            }); ;

/////////////////////////
        }

        [HttpGet("testtest")]
        public IActionResult TestDaily()
        {
            var res = _context.DailyMeals.Where(u => u.UserId == 2);
            var res2 = _context.DailyMeals.AsQueryable();

            ICollection<TesttViewModel> daily;
            daily = res2.Select(d => new TesttViewModel
            {
                Id = d.Id,
                UserId = d.UserId,
                ProductWeight = d.ProductWeight,
                ProductId = d.ProductId,
            }).Where(u => u.UserId == 2).ToList();



            var query = _context.Products.AsQueryable();
                        ICollection<ProductViewModel> products;

            products = query.Select(p => new ProductViewModel
            {
                Id = p.Id,
                Name = p.Name,
                Protein = p.Protein,
                Fat = p.Fat,
                Carbohydrate = p.Carbohydrate,
                Calories = p.Calories,
            }).ToList();

            var result = from d in daily
                         join p in products on d.ProductId equals p.Id
                         select new { UserId = d.UserId, Weight = d.ProductWeight, ProdName = p.Name };

            

          

            var result4 = daily.Join(products, // второй набор
             d => d.ProductId, // свойство-селектор объекта из первого набора
             p => p.Id, // свойство-селектор объекта из второго набора
             (d, p) => new { UserId = d.UserId, Weight = d.ProductWeight, ProdName = p.Name }); // результат

            var result5 = daily.Join(products, 
             d => d.ProductId,
             p => p.Id, 
             (d, p) => new TestJoinViewModel
             { 
                 UserId = d.UserId,
                 ProductWeight = d.ProductWeight, 
                 ProductName = p.Name,
                 ProductProtein = p.Protein
             }); // результат




            return Ok();
        }
    }
}
