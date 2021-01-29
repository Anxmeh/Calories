﻿using Microsoft.AspNetCore.Identity;
using Microsoft.Extensions.DependencyInjection;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CalorieCounter.Entities
{
    public class SeederDB
    {
        public static void SeedData(UserManager<DbUser> userManager, RoleManager<DbRole> roleManager)
        {
            var adminRoleName = "Admin";
            var userRoleName = "User";

            var roleResult = roleManager.FindByNameAsync(adminRoleName).Result;
            if (roleResult == null)
            {
                var roleresult = roleManager.CreateAsync(new DbRole
                {
                    Name = adminRoleName

                }).Result;
            }

            roleResult = roleManager.FindByNameAsync(userRoleName).Result;
            if (roleResult == null)
            {
                var roleresult = roleManager.CreateAsync(new DbRole
                {
                    Name = userRoleName

                }).Result;
            }

            var email = "admin@gmail.com";
            var findUser = userManager.FindByEmailAsync(email).Result;
            if (findUser == null)
            {
                var user = new DbUser
                {
                    Email = email,
                    UserName = email,
                };

                user.UserProfile = new UserProfile()
                {
                    Name = "Petro",
                    Surname = "Petunchik",
                    DateOfBirth = new DateTime(1980, 5, 20),
                    Phone = "+380978515659",
                    RegistrationDate = DateTime.Now,
                    Photo = "person_1.jpg"
                };

                var result = userManager.CreateAsync(user, "Qwerty1-").Result;
                result = userManager.AddToRoleAsync(user, adminRoleName).Result;
            }

            email = "user@gmail.com";
            findUser = userManager.FindByEmailAsync(email).Result;
            if (findUser == null)
            {
                var user = new DbUser
                {
                    Email = email,
                    UserName = email,
                };

                user.UserProfile = new UserProfile()
                {
                    Name = "Natalya",
                    Surname = "Pupenko",
                    DateOfBirth = new DateTime(1982, 10, 7),
                    Phone = "+380670015009",
                    RegistrationDate = DateTime.Now,
                    Photo = "person_2.jpg"
                };

                var result = userManager.CreateAsync(user, "Qwerty1-").Result;
                result = userManager.AddToRoleAsync(user, userRoleName).Result;
            }
        }

        public static void SeedProducts(EFContext _context)
        {
            if (_context.Products.Count() <= 0)
            {
                var products = new List<Product>();
                products.Add(new Product
                {
                    Name = "Сhicken breast",
                    Protein = 23.6,
                    Fat = 1.9,
                    Carbohydrate = 0.4,
                    Calories = 113
                });
                products.Add(new Product
                {
                    Name = "Boilde buckwheat",
                    Protein = 3.9,
                    Fat = 1.4,
                    Carbohydrate = 20.1,
                    Calories = 109
                });
                products.Add(new Product
                {
                    Name = "Oat flakes",
                    Protein = 10.8,
                    Fat = 3.2,
                    Carbohydrate = 59.5,
                    Calories = 310
                });
                products.Add(new Product
                {
                    Name = "Milk 2.5%",
                    Protein = 2.9,
                    Fat = 2.5,
                    Carbohydrate = 4.8,
                    Calories = 53
                });
                products.Add(new Product
                {
                    Name = "Coffee",
                    Protein = 0.2,
                    Fat = 0.5,
                    Carbohydrate = 0.2,
                    Calories = 6
                });
                products.Add(new Product
                {
                    Name = "Dark chocolate",
                    Protein = 6.3,
                    Fat = 31.8,
                    Carbohydrate = 53.7,
                    Calories = 526
                });

                foreach (var product in products)
                    _context.Products.Add(product);
                _context.SaveChanges();
            }
        }

        public static void SeedVitamins(EFContext _context)
        {
            if (_context.Vitamins.Count() <= 0)
            {
                var vitamins = new List<Vitamin>();
                vitamins.Add(new Vitamin
                {
                    VitaminName = "Вітамін D 500МО",
                });
                vitamins.Add(new Vitamin
                {
                    VitaminName = "Вітамін D 2000МО",
                });
                vitamins.Add(new Vitamin
                {
                    VitaminName = "Омега 3 180/120",
                });
                vitamins.Add(new Vitamin
                {
                    VitaminName = "Кальцій 400мг",
                });
                vitamins.Add(new Vitamin
                {
                    VitaminName = "Магній 400мг",
                });


                foreach (var vitamin in vitamins)
                    _context.Vitamins.Add(vitamin);
                _context.SaveChanges();
            }
        }
        //public static void SeedMeal(EFContext _context)
        //{
        //    if (_context.DailyMenus.Count() <= 0)
        //    {
        //        var meal = new DailyMenu{
        //            DateOfMeal = DateTime.Now,
        //            ProductWeight = 300,
        //            ProductId = 1,
        //            UserId = 2 };

        //        _context.DailyMenus.Add(meal);
        //        _context.SaveChanges();
        //    }
        //}

        public static void SeedDataByAS(IServiceProvider services)
        {
            using (var scope = services.GetRequiredService<IServiceScopeFactory>().CreateScope())
            {
                var manager = scope.ServiceProvider.GetRequiredService<UserManager<DbUser>>();
                var managerRole = scope.ServiceProvider.GetRequiredService<RoleManager<DbRole>>();
                var context = scope.ServiceProvider.GetRequiredService<EFContext>();
                SeederDB.SeedData(manager, managerRole);
                SeederDB.SeedProducts(context);
                SeederDB.SeedVitamins(context);

            }
        }
    }
}
