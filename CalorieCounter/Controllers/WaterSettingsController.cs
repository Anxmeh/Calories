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
    public class WaterSettingsController : ControllerBase
    {
        private readonly EFContext _context;
        private readonly IWebHostEnvironment _env;
        public WaterSettingsController(EFContext context, IWebHostEnvironment env)
        {
            _context = context;
            _env = env;
        }

        [HttpGet("watersettings")]
        public IActionResult GetWaterSettings()
        {
            //string userName;
            
            //try
            //{
            //    userName = User.Claims.FirstOrDefault(x => x.Type == "name").Value;
            //}
            //catch (Exception)
            //{
            //    return BadRequest("Потрібно спочатку залогінитися!");
            //}

            //if (string.IsNullOrEmpty(userName))
            //{
            //    return BadRequest("Потрібно спочатку залогінитися!");

            //}

            //var queryUser = _context.Users.Include(x => x.UserProfile).AsQueryable();
            //var user = queryUser.FirstOrDefault(c => c.UserName == userName);

            //if (user == null)
            //{
            //    return BadRequest("Поганий запит!");
            //}
            //long ids = user.Id;
            //  UserProfileView userProfile = new UserProfileView(user);


            // var res = _context.DailyMenus.Where(u => u.UserId == 2);
            var queryWater = _context.WaterSettings.AsQueryable();

    
            var water = _context.WaterSettings.SingleOrDefault(w => w.UserId == 2);


            return Ok(new WaterSettingsViewModel
            {
                Begin = water.Begin,
                End = water.End

            });
        }
    }
}
