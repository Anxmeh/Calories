using CalorieCounter.Entities;
using CalorieCounter.Models;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CalorieCounter.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class DishController : ControllerBase
    {
        private readonly EFContext _context;
        private readonly IWebHostEnvironment _env;
        public DishController(EFContext context, IWebHostEnvironment env)
        {
            _context = context;
            _env = env;
        }

        [HttpGet("calculatedish")]
        public IActionResult GetAllProducts()
        {
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
                DishCarbohydrate = carbs,
                DishFat = fats,
                DishProtein = proteins,
                DishCalories = calories,
                DishWeight = weight
            });
        }

        [HttpGet("productsindish")]
        public IActionResult GetAllProductsInDish()
        {
            var query = _context.ProductsInDish.AsQueryable();

            ICollection<AddDishViewModel> result;

            result = query.Select(d => new AddDishViewModel
            {
               ProductName = d.ProductName,
                ProductProtein = d.ProductProtein,
                ProductFat = d.ProductFat,
                ProductCarbohydrate = d.ProductCarbohydrate,
                ProductCalories = d.ProductCalories,
                ProductWeight = d.ProductWeight
            }).ToList();

            return Ok(result);
        }
    }
}
