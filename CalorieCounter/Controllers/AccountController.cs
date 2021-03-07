﻿using CalorieCounter.Entities;
using CalorieCounter.Helpers;
using CalorieCounter.Models;
using CalorieCounter.Services;
using Google.Apis.Auth;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using MimeKit;
using System;
using System.Collections.Generic;
using System.Drawing.Imaging;
using System.IO;
using System.Linq;
using System.Net.Mail;
using System.Security.Claims;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace CalorieCounter.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AccountController : ControllerBase
    {
        private readonly EFContext _context;
        private readonly UserManager<DbUser> _userManager;
        private readonly SignInManager<DbUser> _signInManager;
        private readonly IJwtTokenService _IJwtTokenService;
        private readonly IWebHostEnvironment _env;

        public AccountController(EFContext context,
           UserManager<DbUser> userManager,
           SignInManager<DbUser> signInManager,
           IJwtTokenService IJwtTokenService,
           IWebHostEnvironment env)
        {
            _userManager = userManager;
            _context = context;
            _signInManager = signInManager;
            _IJwtTokenService = IJwtTokenService;
            _env = env;
        }

        [HttpPost("login")]
        public async Task<IActionResult> Login([FromBody] UserLoginViewModel model)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest("Bad Model");
            }

            var user = _context.Users.FirstOrDefault(u => u.Email == model.Email);
            if (user == null)
            {
                return BadRequest("Даний користувач не знайденний!");
            }

            var queryUser = _context.Users.Include(x => x.UserProfile).AsQueryable();
            var user1 = queryUser.FirstOrDefault(c => c.UserName == user.UserName);

            if (user1 == null)
            {
                return BadRequest("Поганий запит!");
            }
            long ids = user1.Id;
            UserProfileView userProfile = new UserProfileView(user1);
            if (userProfile.FromGoogleLogin)
                return BadRequest("Ви зареєстровані через Google. Скористайтесь відповідною кнопкою для входу");

            var result = _signInManager
                .PasswordSignInAsync(user, model.Password, false, false).Result;

            if (!result.Succeeded)
            {
                return BadRequest("Невірно введений пароль!");
            }

            await _signInManager.SignInAsync(user, isPersistent: false);
            return Ok(
                 new
                 {
                     token = _IJwtTokenService.CreateToken(user)
                 });
        }

        [HttpPost("register")]
        [RequestSizeLimit(100 * 1024 * 1024)]     // set the maximum file size limit to 100 MB
        public async Task<IActionResult> Register([FromBody] UserRegisterViewModel model)
        {

            if (!ModelState.IsValid)
            {
                return BadRequest("Поганий запит");
            }

            var roleName = "User";
            var userReg = _context.Users.FirstOrDefault(x => x.Email == model.Email);
            if (userReg != null)
            {
                return BadRequest("Цей емейл вже зареєстровано!");
            }

            if (model.Email == null)
            {
                return BadRequest("Вкажіть пошту!");
            }
            else
            {
                var testmail = new Regex(@"^([\w\.\-]+)@([\w\-]+)((\.(\w){2,3})+)$");
                if (!testmail.IsMatch(model.Email))
                {
                    return BadRequest("Невірно вказана почта!");
                }
            }
            DbUser user = new DbUser
            {
                Email = model.Email,
                UserName = model.Email
            };
            string ext = ".jpg";
            string fileName = Path.GetRandomFileName() + ext;

            user.UserSettings = new UserSettings()
            {
                Calories = 1500,
                UserCalories = 1500,
                UserCarbohydrate = 150,
                UserFat = 50,
                UserProtein = 113
            };

            user.UserWaterSettings = new WaterSetting()
            {
                BeginHour = 9,
                BeginMinute = 1,
                EndHour = 20,
                EndMinute = 1,
                UserWaterVolume = 2000
            };

            user.UserProfile = new UserProfile()
            {
                Name = "Empty",
                Surname = "Empty",
                DateOfBirth = new DateTime(2000, 1, 1),
                Phone = "+380000000000",
                RegistrationDate = DateTime.Now,
                Photo = null
            };

            if (!String.IsNullOrWhiteSpace(model.ImageBase64))
            {
                user.UserProfile.Photo = fileName;
                var bmp = model.ImageBase64.FromBase64StringToImage();
                var serverPath = _env.ContentRootPath;
                var folerName = "Uploaded";
                var path = Path.Combine(serverPath, folerName);

                if (!Directory.Exists(path))
                {
                    Directory.CreateDirectory(path);
                }

                string filePathSave = Path.Combine(path, fileName);
                bmp.Save(filePathSave, ImageFormat.Jpeg);
            }

            var res = _userManager.CreateAsync(user, model.Password).Result;
            if (!res.Succeeded)
            {
                return BadRequest("Код доступу має складатись з 8 символів, містити мінімум одну велику літеру!");
            }

            res = _userManager.AddToRoleAsync(user, roleName).Result;

            if (!res.Succeeded)
            {
                return BadRequest("Поганий запит!");
            }

            await _signInManager.SignInAsync(user, isPersistent: false);

            return Ok(
                 new
                 {
                     token = _IJwtTokenService.CreateToken(user)
                 });
        }


        [HttpPost("logingoogle")]
        public async Task<IActionResult> LoginGoogle([FromBody] LoginGoogleViewModel model)
        {
            var validPayload = await GoogleJsonWebSignature.ValidateAsync(model.IdToken);

            if (validPayload == null)
                return BadRequest("Поганий запит");
            var user = _context.Users.FirstOrDefault(u => u.Email == validPayload.Email);
            if (user == null)
            {
                var roleName = "User";
                var userReg = _context.Users.FirstOrDefault(x => x.Email == validPayload.Email);

                DbUser dbUser = new DbUser
                {
                    Email = validPayload.Email,
                    UserName = validPayload.Email
                };

                dbUser.UserSettings = new UserSettings()
                {
                    Calories = 1500,
                    UserCalories = 1500,
                    UserCarbohydrate = 150,
                    UserFat = 50,
                    UserProtein = 113
                };
                dbUser.UserWaterSettings = new WaterSetting()
                {
                    BeginHour = 9,
                    BeginMinute = 1,
                    EndHour = 20,
                    EndMinute = 1,
                    UserWaterVolume = 2000
                };
                dbUser.UserProfile = new UserProfile()
                {
                    Name = validPayload.GivenName,
                    Surname = validPayload.FamilyName,
                    DateOfBirth = new DateTime(2000, 1, 1),
                    Phone = "+380000000000",
                    RegistrationDate = DateTime.Now,
                    Photo = null,
                    FromGoogleLogin = true
                };

                string password = Guid.NewGuid().ToString();
                string resP = password.Insert(2, "G");

                var res = _userManager.CreateAsync(dbUser, resP).Result;
                if (!res.Succeeded)
                {
                    return BadRequest("Код доступу має складатись з 8 символів, містити мінімум одну велику літеру!");
                }

                res = _userManager.AddToRoleAsync(dbUser, roleName).Result;

                if (!res.Succeeded)
                {
                    return BadRequest("Поганий запит!");
                }

                await _signInManager.SignInAsync(dbUser, isPersistent: false);

                //var emailMessage = new MimeMessage();

                //emailMessage.From.Add(new MailboxAddress("Peter", "noreply@karpaty.tk"));
                //emailMessage.To.Add(new MailboxAddress("", "77dasha0377@gmail.com"));
                //emailMessage.Subject = "Title---";
                //emailMessage.Body = new TextPart(MimeKit.Text.TextFormat.Html)
                //{
                //    Text = "Efefe---f"
                //};

                //using (var client = new SmtpClient())
                //{
                //    //client.Connect("smtp.gmail.com", 587, true);
                //    //client.Authenticate("itstudentyre@gmail.com", "user@karpaty.tk");
                //    client.Connect("karpaty.tk", 587, true);
                //    client.Authenticate("user@karpaty.tk", "Qwerty-1");
                //    client.Send(emailMessage);

                //    client.Disconnect(true);
                //}

                return Ok(
                     new
                     {
                         token = _IJwtTokenService.CreateToken(dbUser)
                     });
            }

            await _signInManager.SignInAsync(user, isPersistent: false);
            return Ok(
                 new
                 {
                     token = _IJwtTokenService.CreateToken(user)
                 });
        }

        [HttpPost("signout")]
        public async Task<IActionResult> Logout()
        {
            await _signInManager.SignOutAsync();
            return Ok();
        }

        //public void SendMesssage()
        //{
        //    var emailMessage = new MimeMessage();

        //    emailMessage.From.Add(new MailboxAddress("Peter", "noreply@karpaty.tk"));
        //    emailMessage.To.Add(new MailboxAddress("", "77dasha0377@gmail.com"));
        //    emailMessage.Subject = "Title---";
        //    emailMessage.Body = new TextPart(MimeKit.Text.TextFormat.Html)
        //    {
        //        Text = "Hello"
        //    };

        //    using (var client = new SmtpClient())
        //    {
        //        //client.Connect("smtp.gmail.com", 587, true);
        //        //client.Authenticate("itstudentyre@gmail.com", "user@karpaty.tk");
        //        client.Connect("karpaty.tk", 587, true);
        //        client.Authenticate("user@karpaty.tk", "Qwerty-1");
        //        client.Send(emailMessage);

        //        client.Disconnect(true);
        //    }
        //}
    }
}

