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
            return Ok(new ProductViewModel
            {
                Name = product.Name,
                Calories = product.Calories,
                Protein = product.Protein,
                Carbohydrate = product.Carbohydrate,
                Fat = product.Fat
            });
        }
    }
}
