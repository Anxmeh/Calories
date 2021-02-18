using Microsoft.AspNetCore.Identity;
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
                    Name = "Петро",
                    Surname = "Петрунчик",
                    DateOfBirth = new DateTime(1980, 5, 20),
                    Phone = "+380978515659",
                    RegistrationDate = DateTime.Now,
                    Photo = "person_1.jpg"
                };

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
                    EndMinute = 1
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
                    Name = "Наталія",
                    Surname = "Пупенко",
                    DateOfBirth = new DateTime(1982, 10, 7),
                    Phone = "+380670015009",
                    RegistrationDate = DateTime.Now,
                    Photo = "person_2.jpg"
                };

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
                    EndMinute = 1
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
                    Name = "Філе куряче сире",
                    Protein = 23.6,
                    Fat = 1.9,
                    Carbohydrate = 0.4,
                    Calories = 113
                });
                products.Add(new Product
                {
                    Name = "Крупа гречана відварена",
                    Protein = 3.9,
                    Fat = 1.4,
                    Carbohydrate = 20.1,
                    Calories = 109
                });
                products.Add(new Product
                {
                    Name = "Вівсяні пластівці",
                    Protein = 10.8,
                    Fat = 3.2,
                    Carbohydrate = 59.5,
                    Calories = 310
                });
                products.Add(new Product
                {
                    Name = "Молоко 2.5%",
                    Protein = 2.9,
                    Fat = 2.5,
                    Carbohydrate = 4.8,
                    Calories = 53
                });
                products.Add(new Product
                {
                    Name = "Кава",
                    Protein = 0.2,
                    Fat = 0.5,
                    Carbohydrate = 0.2,
                    Calories = 6
                });
                products.Add(new Product
                {
                    Name = "Чорний шоколад",
                    Protein = 6.3,
                    Fat = 31.8,
                    Carbohydrate = 53.7,
                    Calories = 526
                });
                products.Add(new Product
                {
                    Name = "Сир кисломолочний 3%",
                    Protein = 18,
                    Fat = 3,
                    Carbohydrate = 1.7,
                    Calories = 106
                });
                products.Add(new Product
                {
                    Name = "Йогурт без цукру 3,2%",
                    Protein = 5,
                    Fat = 3.2,
                    Carbohydrate = 3.5,
                    Calories = 63
                });
                products.Add(new Product
                {
                    Name = "Яйце куряче варене",
                    Protein = 12.9,
                    Fat = 11.6,
                    Carbohydrate = 0.8,
                    Calories = 160
                });
                products.Add(new Product
                {
                    Name = "Яблуко",
                    Protein = 0.4,
                    Fat = 0.4,
                    Carbohydrate = 9.8,
                    Calories = 44
                });
                products.Add(new Product
                {
                    Name = "Апельсин",
                    Protein = 0.9,
                    Fat = 0.2,
                    Carbohydrate = 8.1,
                    Calories = 38
                });
                products.Add(new Product
                {
                    Name = "Бедро куряче запечене",
                    Protein = 25.9,
                    Fat = 10.9,
                    Carbohydrate = 0,
                    Calories = 202
                });
                products.Add(new Product
                {
                    Name = "Карп",
                    Protein = 16.0,
                    Fat = 5.3,
                    Carbohydrate = 0,
                    Calories = 112
                });
                products.Add(new Product
                {
                    Name = "Форель",
                    Protein = 21.0,
                    Fat = 7.3,
                    Carbohydrate = 0,
                    Calories = 147
                });
                products.Add(new Product
                {
                    Name = "Картопля в мундирах",
                    Protein = 2.5,
                    Fat = 0.4,
                    Carbohydrate = 16.3,
                    Calories = 87
                });
                products.Add(new Product
                {
                    Name = "Цукор",
                    Protein = 0,
                    Fat = 0,
                    Carbohydrate = 99.8,
                    Calories = 399
                });
                products.Add(new Product
                {
                    Name = "Олія соняшникова",
                    Protein = 0,
                    Fat = 99.9,
                    Carbohydrate = 0,
                    Calories = 899
                });
                products.Add(new Product
                {
                    Name = "Масло вершкове 72.5%",
                    Protein = 0.8,
                    Fat = 72.5,
                    Carbohydrate = 1.3,
                    Calories = 661
                });
                products.Add(new Product
                {
                    Name = "Масло вершкове 82%",
                    Protein = 0.6,
                    Fat = 82,
                    Carbohydrate = 0.8,
                    Calories = 748
                });
                products.Add(new Product
                {
                    Name = "Хліб білий",
                    Protein = 7.9,
                    Fat = 1,
                    Carbohydrate = 48.3,
                    Calories = 234
                });
                products.Add(new Product
                {
                    Name = "Капуста пекінська",
                    Protein = 1.2,
                    Fat = 0.2,
                    Carbohydrate = 3.2,
                    Calories = 19
                });
                products.Add(new Product
                {
                    Name = "Сочевиця червона",
                    Protein = 24.2,
                    Fat = 1.7,
                    Carbohydrate = 7.6,
                    Calories = 325
                });
                products.Add(new Product
                {
                    Name = "Рис",
                    Protein = 7.5,
                    Fat = 2.6,
                    Carbohydrate = 62.3,
                    Calories = 303
                });
                products.Add(new Product
                {
                    Name = "Печиво вівсяне",
                    Protein = 5.8,
                    Fat = 14.7,
                    Carbohydrate = 70.5,
                    Calories = 438
                });
                products.Add(new Product
                {
                    Name = "Печиво пісочне",
                    Protein = 7.2,
                    Fat = 12.7,
                    Carbohydrate = 59,
                    Calories = 379
                });
                products.Add(new Product
                {
                    Name = "Мед",
                    Protein = 0.8,
                    Fat = 0,
                    Carbohydrate = 80.3,
                    Calories = 324
                });
                products.Add(new Product
                {
                    Name = "Торт медовик",
                    Protein = 5.9,
                    Fat = 28,
                    Carbohydrate = 48.1,
                    Calories = 468
                });
                products.Add(new Product
                {
                    Name = "Торт Наполеон",
                    Protein = 6,
                    Fat = 29.2,
                    Carbohydrate = 40.2,
                    Calories = 447
                });
                products.Add(new Product
                {
                    Name = "Борошно в/г",
                    Protein = 10.8,
                    Fat = 1.3,
                    Carbohydrate = 69.9,
                    Calories = 335
                });
                products.Add(new Product
                {
                    Name = "Яйце куряче сире",
                    Protein = 12.7,
                    Fat = 10.9,
                    Carbohydrate = 0.7,
                    Calories = 157
                });
                products.Add(new Product
                {
                    Name = "Гриби печериці",
                    Protein = 4.3,
                    Fat = 1,
                    Carbohydrate = 1,
                    Calories = 30
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
