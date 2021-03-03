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
    public class SettingsController : ControllerBase
    {
        const double PROREIN_CALORIES = 4.1;
        const double FAT_CALORIES = 9.29;
        const double CARB_CALORIES = 4.1;

        private readonly EFContext _context;
        private readonly IWebHostEnvironment _env;
        public SettingsController(EFContext context, IWebHostEnvironment env)
        {
            _context = context;
            _env = env;
        }

        [HttpPost("settings")]
        public IActionResult GetSettings()
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

            var query = _context.Users.Include(x => x.UserSettings).AsQueryable();
            var user = query.FirstOrDefault(c => c.UserName == userName);

            if (user == null)
            {
                return BadRequest("Поганий запит!");
            }

            UserSettingsView userSettings = new UserSettingsView(user);
            return Ok(userSettings);
        }

        [HttpPost("updatesettings")]
        public IActionResult SettingsUpdate([FromBody] UserSettingsView model)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest("Bad Model");
            }

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

            var query = _context.Users.Include(x => x.UserSettings).AsQueryable();
            var user = query.FirstOrDefault(c => c.UserName == userName);

            if (user == null)
            {
                return BadRequest("Поганий запит!");
            }

            user.UserSettings.Age = model.Age;
            user.UserSettings.Weight = model.Weight;
            user.UserSettings.Height = model.Height;
            user.UserSettings.Chest = model.Chest;
            user.UserSettings.Waist = model.Waist;
            user.UserSettings.Hips = model.Hips;
            user.UserSettings.Hip = model.Hip;
            user.UserSettings.Shin = model.Shin;
            user.UserSettings.Wrist = model.Wrist;
            user.UserSettings.Forearm = model.Forearm;
            user.UserSettings.Neck = model.Neck;
            user.UserSettings.Sex = model.Sex;
            user.UserSettings.Activity = model.Activity;
            user.UserSettings.UserCalories = model.UserCalories;

            double heightM = user.UserSettings.Height / 100;
            user.UserSettings.Bmi = Math.Round(user.UserSettings.Weight / (heightM * heightM),2);

            if (user.UserSettings.Sex == true) //male
                user.UserSettings.Calories = Math.Round(((88.36 + (13.4 * user.UserSettings.Weight) +
                    (4.8 * user.UserSettings.Height) - (5.7 * user.UserSettings.Age)) * user.UserSettings.Activity), 2);

            else if (user.UserSettings.Sex == false) //female 
                user.UserSettings.Calories = Math.Round(((447.6 + (9.2 * user.UserSettings.Weight) +
                (3.1 * user.UserSettings.Height) - (4.3 * user.UserSettings.Age)) * user.UserSettings.Activity), 2);

            if (user.UserSettings.Sex == true)
                user.UserSettings.FatPercentage = Math.Round(user.UserSettings.FatPercentage = 495 / (1.0324 - 0.19077 *
                    (Math.Log10(user.UserSettings.Waist - user.UserSettings.Neck)) +
                   0.15456 * (Math.Log10(user.UserSettings.Height))) - 450, 2);
            else if (user.UserSettings.Sex == false) 
                user.UserSettings.FatPercentage = Math.Round(user.UserSettings.FatPercentage = 495 / (1.29579 - 0.35004 * (Math.Log10(user.UserSettings.Waist + user.UserSettings.Hips -
                     user.UserSettings.Neck)) + 0.22100 * (Math.Log10(user.UserSettings.Height))) - 450, 2);

            _context.SaveChanges();

            var result = new UserSettingsView(user);
            return Ok(result);
        }

        [HttpPost("setusercalories")]
        public IActionResult SetUserCalories([FromBody] double userCalories)
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

            var query = _context.Users.Include(x => x.UserSettings).AsQueryable();
            var user = query.FirstOrDefault(c => c.UserName == userName);

            if (user == null)
            {
                return BadRequest("Поганий запит!");
            }

            user.UserSettings.UserCalories = userCalories;
            user.UserSettings.UserProtein = user.UserSettings.UserCalories * 0.3 / PROREIN_CALORIES;
            user.UserSettings.UserFat = user.UserSettings.UserCalories * 0.3 / FAT_CALORIES;
            user.UserSettings.UserCarbohydrate = user.UserSettings.UserCalories * 0.4 / CARB_CALORIES;
            _context.SaveChanges();
            UserSettingsView userSettings = new UserSettingsView(user);
           
            return Ok(userSettings);
        }
    }
}
