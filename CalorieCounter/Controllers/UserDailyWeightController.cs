using CalorieCounter.Entities;
using CalorieCounter.Models;
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
    public class UserDailyWeightController : ControllerBase
    {
        private readonly EFContext _context;

        public UserDailyWeightController(EFContext context)
        {
            _context = context;
        }

        [HttpGet("userdailyweights")]
        public IActionResult GetUserDailyWeights()
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

            var queryUser = _context.Users.AsQueryable();
            var user = queryUser.FirstOrDefault(c => c.UserName == userName);

            if (user == null)
            {
                return BadRequest("Поганий запит!");
            }

            var queryWeights = _context.UserDailyWeights.AsQueryable();
            ICollection<UserDailyWeightsViewModel> dailyWeights;
            dailyWeights = queryWeights.Select(d => new UserDailyWeightsViewModel
            {
                UserId = d.UserId,
                Weight = d.Weight,
                DateOfWeight = d.DateOfWeight.Date,
            }).Where(u => u.UserId == user.Id)
            .OrderBy(m => m.DateOfWeight)
            .ToList();

            return Ok(dailyWeights);
        }

        [HttpPost("adduserdailyweight")]
        public IActionResult AddUserDailyWeight([FromBody] AddUserDailyWeightsViewModel model)
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

            var queryUser = _context.Users.AsQueryable();
            var user = queryUser.FirstOrDefault(c => c.UserName == userName);

            if (user == null)
            {
                return BadRequest("Поганий запит!");
            }

            var dailyWeight = _context.UserDailyWeights.SingleOrDefault(d => d.UserId == user.Id && d.DateOfWeight == model.DateOfWeight);
            if (dailyWeight != null)
                return BadRequest(new { invalid = "Уже є дані за цей день, Ви можете змінити їх при потребі" });
            dailyWeight = new UserDailyWeight
            {
                UserId = user.Id,
                DateOfWeight = model.DateOfWeight,
                Weight = model.Weight,

            };
            _context.UserDailyWeights.Add(dailyWeight);
            _context.SaveChanges();

            var queryWeights = _context.UserDailyWeights.AsQueryable();
            ICollection<UserDailyWeightsViewModel> dailyWeights;
            dailyWeights = queryWeights.Select(d => new UserDailyWeightsViewModel
            {
                UserId = d.UserId,
                Weight = d.Weight,
                DateOfWeight = d.DateOfWeight,
            }).Where(u => u.UserId == user.Id)
            .ToList();
            return Ok(dailyWeights);
        }

        [HttpPost("edituserdailyweight")]
        public IActionResult EditUserDailyWeight([FromBody] AddUserDailyWeightsViewModel model)
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

            var queryUser = _context.Users.AsQueryable();
            var user = queryUser.FirstOrDefault(c => c.UserName == userName);

            if (user == null)
            {
                return BadRequest("Поганий запит!");
            }

            var dailyWeight = _context.UserDailyWeights.SingleOrDefault(d => d.UserId == user.Id && d.DateOfWeight.Date == model.DateOfWeight.Date);
            if (dailyWeight == null)
            {
                dailyWeight = new UserDailyWeight
                {
                    UserId = user.Id,
                    DateOfWeight = model.DateOfWeight,
                    Weight = model.Weight,

                };
                _context.UserDailyWeights.Add(dailyWeight);
                _context.SaveChanges();
            }
            else
            {

                dailyWeight.Weight = model.Weight;
                _context.SaveChanges();
            }

            return Ok(new AddUserDailyWeightsViewModel
            {
                Weight = dailyWeight.Weight,
                DateOfWeight = dailyWeight.DateOfWeight,
            });
        }

        [HttpPost("userweight")]
        public IActionResult GetUserWeight([FromBody] DateTime date)
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

            var queryUser = _context.Users.AsQueryable();
            var user = queryUser.FirstOrDefault(c => c.UserName == userName);

            if (user == null)
            {
                return BadRequest("Поганий запит!");
            }

            var dailyWeight = _context.UserDailyWeights.SingleOrDefault(d => d.UserId == user.Id && d.DateOfWeight.Date == date.Date);
            if (dailyWeight == null)
            {
                var weight = new UserDailyWeight
                {
                    Weight = 0,
                    DateOfWeight = date,
                    UserId = user.Id
                };
                _context.UserDailyWeights.Add(weight);
                _context.SaveChanges();

                return Ok(new AddUserDailyWeightsViewModel
                {
                    Weight = weight.Weight,
                    DateOfWeight = weight.DateOfWeight,
                });
            }

            return Ok(new AddUserDailyWeightsViewModel
            {
                Weight = dailyWeight.Weight,
                DateOfWeight = dailyWeight.DateOfWeight,
            });
        }
    }
}
