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

namespace CalorieCounter.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class WaterController : ControllerBase
    {
        private readonly EFContext _context;
        private readonly IWebHostEnvironment _env;
        public WaterController(EFContext context, IWebHostEnvironment env)
        {
            _context = context;
            _env = env;
        }

        [HttpPost("dailywater")]
        public IActionResult GetDailyWater([FromBody] DateTime date)
        {
            string userName;
            string dateWater = date.Date.ToString();
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
            //  UserProfileView userProfile = new UserProfileView(user);


            // var res = _context.DailyMenus.Where(u => u.UserId == 2);
            var queryWater = _context.WaterCounters.AsQueryable();

            ICollection<WaterViewModel> dailyWater;

            // ICollection<TesttViewModel> daily;
            dailyWater = queryWater.Select(d => new WaterViewModel
            {
                UserId = d.UserId,
                WaterVolume = d.WaterVolume,
                DateOfDrink = d.DateOfDrink,
            }).Where(u => u.UserId == user.Id && u.DateOfDrink.Date == date.Date).ToList();

            var water = _context.WaterCounters.SingleOrDefault(w => w.DateOfDrink.Date == date.Date && w.UserId == user.Id);


            return Ok(new AddWaterViewModel
            {
                            DateOfDrink = water.DateOfDrink,
                WaterVolume = water.WaterVolume

            });
        }

        [HttpPost("addwater")]
        public IActionResult AddWater([FromBody] AddWaterViewModel model)
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


            //var product = _context.Products.SingleOrDefault(p => p.Name == model.ProductName);
            //if (product == null)
            //    return BadRequest(new { invalid = "Не знайдено у базі" });

            var water = _context.WaterCounters.SingleOrDefault(w => w.DateOfDrink.Date == model.DateOfDrink.Date && w.UserId == user.Id);
            int newWater;
            if (water == null)
            {
                var addedWater = new WaterCounter
                {
                    UserId = user.Id,
                    DateOfDrink = model.DateOfDrink,
                    WaterVolume = model.WaterVolume                   
                };
                newWater = addedWater.WaterVolume;
                _context.WaterCounters.Add(addedWater);
                _context.SaveChanges();
            }

            else
            {
                water.WaterVolume += model.WaterVolume;
                newWater = water.WaterVolume;
                _context.SaveChanges();
            }

          
            return Ok(new AddWaterViewModel
            {
               // UserId = user.Id,
                DateOfDrink = model.DateOfDrink,
                WaterVolume = newWater
               
            });

        }
    }
}
