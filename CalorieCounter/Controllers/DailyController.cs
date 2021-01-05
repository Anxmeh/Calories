using CalorieCounter.Entities;
using CalorieCounter.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CalorieCounter.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    [Authorize]
    public class DailyController : ControllerBase
    {
        private readonly EFContext _context;
        private readonly IWebHostEnvironment _env;
        public DailyController(EFContext context, IWebHostEnvironment env)
        {
            _context = context;
            _env = env;
        }

        public IActionResult GetAllProducts()
        {


            var queryProducts = _context.Products.AsQueryable();
            ICollection<ProductViewModel> result;
            result = queryProducts.Select(p => new ProductViewModel
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

        [HttpPost("dailymenu")]
        public IActionResult GetDailyMenu([FromBody] DateTime date)
        {
            string userName;
            string daten = date.Date.ToString();
            try
            {
                userName = User.Claims.FirstOrDefault(x => x.Type == "name").Value;
            }
            catch (Exception)
            {
                return BadRequest("Потрібно спочатку залогінитися!");
            }

            if (string.IsNullOrEmpty(userName))
            {
                return BadRequest("Потрібно спочатку залогінитися!");

            }

            var queryUser = _context.Users.Include(x => x.UserProfile).AsQueryable();
            var user = queryUser.FirstOrDefault(c => c.UserName == userName);

            if (user == null)
            {
                return BadRequest("Поганий запит!");
            }
            long ids = user.Id;
            UserProfileView userProfile = new UserProfileView(user);


            // var res = _context.DailyMenus.Where(u => u.UserId == 2);
            var queryMenu = _context.DailyMeals.AsQueryable();

            ICollection<TesttViewModel> daily;
            daily = queryMenu.Select(d => new TesttViewModel
            {
                Id = d.Id,
                UserId = d.UserId,
                ProductWeight = d.ProductWeight,
                ProductId = d.ProductId,
                Date = d.DateOfMeal
            }).Where(u => u.UserId == user.Id && u.Date.Date == date.Date).ToList();
                        
            var queryProducts = _context.Products.AsQueryable();
          
            ICollection<ProductViewModel> products;

            products = queryProducts.Select(p => new ProductViewModel
            {
                Id = p.Id,
                Name = p.Name,
                Protein = p.Protein,
                Fat = p.Fat,
                Carbohydrate = p.Carbohydrate,
                Calories = p.Calories,
            }).ToList();
          
            var result5 = daily.Join(products,
             d => d.ProductId,
             p => p.Id,
             (d, p) => new DailyMenuViewModel
             {
                 UserId = d.UserId,
                 ProductWeight = d.ProductWeight,
                 ProductName = p.Name,
                 ProductProtein = p.Protein * d.ProductWeight/100,
                 ProductCalories = p.Calories* d.ProductWeight / 100,
                 ProductCarbohydrate = p.Carbohydrate*d.ProductWeight / 100,
                 ProductFat = p.Fat* d.ProductWeight / 100,
                 ProductId = p.Id,
                 DateOfMeal = d.Date
                 //TotalWeight = 0,
                 //TotalCalories = 0
             }); // результат

            CalculateDailyViewModel dailyMenu = new CalculateDailyViewModel();

            foreach (var item in result5)
            {
                dailyMenu.TotalWeight += item.ProductWeight;
                dailyMenu.TotalCalories += item.ProductCalories;
            }


            return Ok(result5);
        }

        //[HttpPost("calculatedaily")]
        //public IActionResult CalculateDaily([FromBody] AddProductDailyViewModel model)
        //{
        //    CalculateDailyViewModel dailyMenu = new CalculateDailyViewModel();

        //    foreach (var item in result5)
        //    {
        //        dailyMenu.TotalWeight += item.ProductWeight;
        //        dailyMenu.TotalCalories += item.ProductCalories;
        //    }

        //}


        [HttpPost("adddailyproduct")]
        public IActionResult AddProductToMenu([FromBody] AddProductDailyViewModel model)
        {
            string userName;
            try
            {
                userName = User.Claims.FirstOrDefault(x => x.Type == "name").Value;
            }
            catch (Exception)
            {
                return BadRequest("Потрібно спочатку залогінитися!");
            }

            if (string.IsNullOrEmpty(userName))
            {
                return BadRequest("Потрібно спочатку залогінитися!");

            }

            var queryUser = _context.Users.Include(x => x.UserProfile).AsQueryable();
            var user = queryUser.FirstOrDefault(c => c.UserName == userName);

            if (user == null)
            {
                return BadRequest("Поганий запит!");
            }
            long ids = user.Id;
            UserProfileView userProfile = new UserProfileView(user);



            var product = _context.Products.SingleOrDefault(p => p.Name == model.ProductName);
            if (product == null)
                return BadRequest(new { invalid = "Не знайдено у базі" });

            var addedProductmodel = new AddProductDailyViewModel
            {
                //ProductId = model.ProductId,
                ProductName = model.ProductName,
                ProductCalories = model.ProductCalories * model.ProductWeight / 100,
                ProductCarbohydrate = model.ProductCarbohydrate * model.ProductWeight / 100,
                ProductFat = model.ProductFat * model.ProductWeight / 100,
                ProductProtein = model.ProductProtein * model.ProductWeight / 100,
                ProductWeight = model.ProductWeight
                
            };

            var addedProduct = new DailyMeal
            {
              //  Id = 4,
                UserId = user.Id,
                DateOfMeal = model.DateOfMeal,
                ProductWeight = model.ProductWeight,
                ProductId = product.Id

            };


            _context.DailyMeals.Add(addedProduct);
            _context.SaveChanges();

            return Ok(new AddProductDailyViewModel
            {
               // ProductId = model.ProductId,
                ProductName = model.ProductName,
                ProductCalories = model.ProductCalories * model.ProductWeight / 100,
                ProductCarbohydrate = model.ProductCarbohydrate * model.ProductWeight / 100,
                ProductFat = model.ProductFat * model.ProductWeight / 100,
                ProductProtein = model.ProductProtein * model.ProductWeight / 100,
                ProductWeight = model.ProductWeight
            });
        }

        [HttpPost("removedailyproduct")]
        public IActionResult RemoveProduct([FromBody] RemoveProductDailyViewModel model)
        {

            string userName;
            try
            {
                userName = User.Claims.FirstOrDefault(x => x.Type == "name").Value;
            }
            catch (Exception)
            {
                return BadRequest("Потрібно спочатку залогінитися!");
            }

            if (string.IsNullOrEmpty(userName))
            {
                return BadRequest("Потрібно спочатку залогінитися!");

            }
            RemoveProductDailyViewModel model2 = model;

            var queryUser = _context.Users.Include(x => x.UserProfile).AsQueryable();
            var user = queryUser.FirstOrDefault(c => c.UserName == userName);

            if (user == null)
            {
                return BadRequest("Поганий запит!");
            }
            long ids = user.Id;
            UserProfileView userProfile = new UserProfileView(user);
            
            var product = _context.DailyMeals.SingleOrDefault(p => p.ProductId == model.ProductId && p.DateOfMeal.Date == model.DateOfMeal.Date && p.UserId == user.Id);
            if (product != null)
            {
                _context.DailyMeals.Remove(product);
                _context.SaveChanges();
            }
            else
                return BadRequest(new { invalid = "Не знайдено" });

            //////////
            var queryMenu = _context.DailyMeals.AsQueryable();

            ICollection<TesttViewModel> daily;
            daily = queryMenu.Select(d => new TesttViewModel
            {
                Id = d.Id,
                UserId = d.UserId,
                ProductWeight = d.ProductWeight,
                ProductId = d.ProductId,
                Date = d.DateOfMeal
            }).Where(u => u.UserId == user.Id).ToList();

            var queryProducts = _context.Products.AsQueryable();

            ICollection<ProductViewModel> products;

            products = queryProducts.Select(p => new ProductViewModel
            {
                Id = p.Id,
                Name = p.Name,
                Protein = p.Protein,
                Fat = p.Fat,
                Carbohydrate = p.Carbohydrate,
                Calories = p.Calories,
            }).ToList();

            var result5 = daily.Join(products,
             d => d.ProductId,
             p => p.Id,
             (d, p) => new DailyMenuViewModel
             {
                 UserId = d.UserId,
                 ProductWeight = d.ProductWeight,
                 ProductName = p.Name,
                 ProductProtein = p.Protein * d.ProductWeight / 100,
                 ProductCalories = p.Calories * d.ProductWeight / 100,
                 ProductCarbohydrate = p.Carbohydrate * d.ProductWeight / 100,
                 ProductFat = p.Fat * d.ProductWeight / 100,
                 ProductId = p.Id,
                 DateOfMeal = d.Date
                 //TotalWeight = 0,
                 //TotalCalories = 0
             }); // результат

            CalculateDailyViewModel dailyMenu = new CalculateDailyViewModel();

            foreach (var item in result5)
            {
                dailyMenu.TotalWeight += item.ProductWeight;
                dailyMenu.TotalCalories += item.ProductCalories;
            }


            return Ok(result5);
            /////////////






          //  return Ok();
        }
    }
}
