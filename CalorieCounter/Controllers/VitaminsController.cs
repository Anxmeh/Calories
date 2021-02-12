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
    public class VitaminsController : ControllerBase
    {
        private readonly EFContext _context;
        private readonly IWebHostEnvironment _env;

        public VitaminsController(EFContext context, IWebHostEnvironment env)
        {
            _context = context;
            _env = env;
        }

        [HttpGet("allvitamins")]
        public IActionResult GetAllVitamins()
        {
            var query = _context.Vitamins.AsQueryable();

            ICollection<GetVitaminsViewModel> result;

            result = query.Select(p => new GetVitaminsViewModel
            {
                Id = p.Id,
                VitaminName = p.VitaminName,

            }).ToList();

            return Ok(result);
        }

        [HttpGet("myvitamins")]
        public IActionResult GetMyVitamins()
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

            ICollection<VitaminSettingsViewModel> userVitamins;
            var queryVitaminSettings = _context.VitaminSettings.AsQueryable();
            userVitamins = queryVitaminSettings.Select(d => new VitaminSettingsViewModel
            {
                UserId = d.UserId,
                VitaminId = d.VitaminId,
                Amount = d.Amount,

            }).Where(u => u.UserId == user.Id).ToList();

            var queryVitamins = _context.Vitamins.AsQueryable();
            ICollection<VitaminsViewModel> allVitamins;

            allVitamins = queryVitamins.Select(p => new VitaminsViewModel
            {
                Id = p.Id,
                VitaminName = p.VitaminName,

            }).ToList();

            var result = userVitamins.Join(allVitamins,
             d => d.VitaminId,
             p => p.Id,
             (d, p) => new AddMyVitaminsViewModel
             {
                 VitaminId = d.VitaminId,
                 Amount = d.Amount,
                 VitaminName = p.VitaminName,
             }); // результат


            return Ok(result);
        }

        [HttpPost("сhangemyvitamin")]
        public IActionResult ChangeMyVitamin([FromBody] AddMyVitaminsViewModel model)
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

            var vitamin = _context.VitaminSettings.SingleOrDefault(p => p.VitaminId == model.VitaminId && p.UserId == user.Id);
            if (vitamin == null)
                return BadRequest(new { invalid = "Не знайдено" });
            vitamin.Amount = model.Amount;
            //vitamin = new VitaminSettings
            //{
            //    VitaminId = model.VitaminId,
            //    Amount = model.Amount,
            //    UserId = user.Id

            //};
            //_context.VitaminSettings.Add(vitamin);
            _context.SaveChanges();

            return Ok(new AddMyVitaminsViewModel
            {
                VitaminId = model.VitaminId,
                VitaminName = model.VitaminName,
                Amount = model.Amount,
            });
        }

        [HttpPost("addmyvitamin")]
        public IActionResult AddMyVitamin([FromBody] AddMyVitaminsViewModel model)

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
            // UserProfileView userProfile = new UserProfileView(user);


            var vitamin = _context.VitaminSettings.SingleOrDefault(p => p.VitaminId == model.VitaminId && p.UserId == user.Id);
            if (vitamin != null)
                return BadRequest(new { invalid = "Уже є у базі" });
            vitamin = new VitaminSettings
            {
                VitaminId = model.VitaminId,
                Amount = model.Amount,
                UserId = user.Id

            };
            _context.VitaminSettings.Add(vitamin);
            _context.SaveChanges();

            return Ok(new AddMyVitaminsViewModel
            {
                VitaminId = model.VitaminId,
                VitaminName = model.VitaminName,
                Amount = model.Amount,
            });
        }

        [HttpPost("removemyvitamin")]
        public IActionResult RemoveMyVitamin([FromBody] long vitaminId)
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

            var vitamin = _context.VitaminSettings.SingleOrDefault(v => v.VitaminId == vitaminId && v.UserId == user.Id);
            if (vitamin != null)
            {
                _context.VitaminSettings.Remove(vitamin);
                _context.SaveChanges();
            }
            else
                return BadRequest(new { invalid = "Не знайдено" });

            return Ok();
        }



        [HttpPost("dailyvitamins")]
        public IActionResult GetDailyVitamins([FromBody] DateTime date)
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
            // long ids = user.Id;
            // UserProfileView userProfile = new UserProfileView(user);


            // var res = _context.DailyMenus.Where(u => u.UserId == 2);
            var queryDailyVitamins = _context.UserVitamins.AsQueryable();

            ICollection<DailyVitaminsViewModel> daily;
            daily = queryDailyVitamins.Select(d => new DailyVitaminsViewModel
            {
                Id = d.Id,
                UserId = d.UserId,
                Amount = d.Amount,
                VitaminId = d.VitaminId,
                IsTaken = d.IsTaken,
                DateOfVitamin = d.DateOfVitamin
            }).Where(u => u.UserId == user.Id && u.DateOfVitamin.Date == date.Date).ToList();



            ICollection<VitaminSettingsViewModel> userVitamins;
            var queryVitaminSettings = _context.VitaminSettings.AsQueryable();
            userVitamins = queryVitaminSettings.Select(d => new VitaminSettingsViewModel
            {
                UserId = d.UserId,
                VitaminId = d.VitaminId,
                Amount = d.Amount,

            }).Where(u => u.UserId == user.Id).ToList();

            if (daily.Count == 0)
            {
                var queryDailyVitaminsN = _context.VitaminSettings.AsQueryable();

                daily = queryDailyVitaminsN.Select(d => new DailyVitaminsViewModel
                {
                    Id = d.Id,
                    UserId = d.UserId,
                    Amount = d.Amount,
                    VitaminId = d.VitaminId,
                    IsTaken = false,
                    DateOfVitamin = date.Date
                }).Where(u => u.UserId == user.Id).ToList();

                foreach (var item in daily)
                {
                    var vit = new UserVitamins()
                    {
                        Amount = item.Amount,
                        VitaminId = item.VitaminId,
                        UserId = item.UserId,
                        DateOfVitamin = item.DateOfVitamin

                    };
                    _context.UserVitamins.Add(vit);
                    _context.SaveChanges();
                }

                var queryVitamins1 = _context.Vitamins.AsQueryable();

                ICollection<VitaminsViewModel> vitamins1;

                vitamins1 = queryVitamins1.Select(p => new VitaminsViewModel
                {
                    Id = p.Id,
                    VitaminName = p.VitaminName,
                }).ToList();

                var result = daily.Join(vitamins1,
                 d => d.VitaminId,
                 p => p.Id,
                 (d, p) => new DailyVitaminsNameViewModel
                 {
                     UserId = d.UserId,
                     VitaminName = p.VitaminName,
                     Amount = d.Amount,
                     IsTaken = d.IsTaken,
                     DateOfVitamin = d.DateOfVitamin
                 }); // результат

                return Ok(result);

            }

            if (userVitamins.Count > daily.Count)
            {
                
                
                foreach (var vitamin in userVitamins)
                    {

                   var temp = daily.FirstOrDefault(v => v.VitaminId == vitamin.VitaminId);
                    if (temp == null)
                    {
                        var vit = new UserVitamins()
                        {
                            Amount = vitamin.Amount,
                            VitaminId = vitamin.VitaminId,
                            UserId = user.Id,
                            DateOfVitamin = date.Date

                        };
                        _context.UserVitamins.Add(vit);
                        _context.SaveChanges();



                        //daily.Add(new DailyVitaminsViewModel
                        //{
                        //                                UserId = user.Id,
                        //    Amount = vitamin.Amount,
                        //    VitaminId = vitamin.VitaminId,
                        //    IsTaken = false,
                        //    DateOfVitamin = date.Date
                        //});

                         queryDailyVitamins = _context.UserVitamins.AsQueryable();

                        
                        daily = queryDailyVitamins.Select(d => new DailyVitaminsViewModel
                        {
                            Id = d.Id,
                            UserId = d.UserId,
                            Amount = d.Amount,
                            VitaminId = d.VitaminId,
                            IsTaken = d.IsTaken,
                            DateOfVitamin = d.DateOfVitamin
                        }).Where(u => u.UserId == user.Id && u.DateOfVitamin.Date == date.Date).ToList();
                    }
                }
            }

          

            var queryVitamins = _context.Vitamins.AsQueryable();

            ICollection<VitaminsViewModel> vitamins;

            vitamins = queryVitamins.Select(p => new VitaminsViewModel
            {
                Id = p.Id,
                VitaminName = p.VitaminName,
            }).ToList();

            var result5 = daily.Join(vitamins,
             d => d.VitaminId,
             p => p.Id,
             (d, p) => new DailyVitaminsNameViewModel
             {
                 UserId = d.UserId,
                 VitaminId = d.VitaminId,
                 VitaminName= p.VitaminName,
                 Amount = d.Amount,
                 IsTaken = d.IsTaken,
                 DateOfVitamin = d.DateOfVitamin
                             }); // результат

                        return Ok(result5);
        }

        [HttpPost("checkvitamin")]
        public IActionResult CheckVitamin([FromBody] DailyVitaminsCheckViewModel model)
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
            //long ids = user.Id;

            var vitamin = _context.UserVitamins.SingleOrDefault(p => p.VitaminId == model.VitaminId && 
            p.UserId == user.Id && p.DateOfVitamin.Date == model.DateOfVitamin.Date);
            if (vitamin == null)
                return BadRequest(new { invalid = "Не знайдено" });

            vitamin.IsTaken = model.IsTaken;
            _context.SaveChanges();

            return Ok(new DailyVitaminsCheckViewModel
            {
                VitaminId = model.VitaminId,
                DateOfVitamin = model.DateOfVitamin,
                IsTaken = model.IsTaken,
            });
        }
    }
}
