using CalorieCounter.Entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CalorieCounter.Models
{
    public class UserSettingsView
    {
        public double Age { get; set; }
        public double Weight { get; set; }
        public double Height { get; set; }
        public double Chest { get; set; }
        public double Waist { get; set; }
        public double Hips { get; set; }
        public double Hip { get; set; }
        public double Shin { get; set; }
        public double Wrist { get; set; }
        public double Forearm { get; set; }
        public double Neck { get; set; }
        public double Calories { get; set; }
        public double Bmi { get; set; }
        public double FatPercentage { get; set; }
        public bool Sex { get; set; }
        public double Activity { get; set; }
        public double UserCalories { get; set; }
        public double UserFat { get; set; }
        public double UserProtein { get; set; }
        public double UserCarbohydrate { get; set; }
        public UserSettingsView() { }
        public UserSettingsView(DbUser user)
        {
            UserSettings settings = user.UserSettings;
            Age = settings.Age;
            Weight = settings.Weight;
            Height = settings.Height;
            Chest = settings.Chest;
            Waist = settings.Waist;
            Hips = settings.Hips;
            Hip = settings.Hip;
            Shin = settings.Shin;
            Wrist = settings.Wrist;
            Forearm = settings.Forearm;
            Neck = settings.Neck;
            Calories = settings.Calories;
            Bmi = settings.Bmi;
            FatPercentage = settings.FatPercentage;
            Sex = settings.Sex;
            Activity = settings.Activity;
            UserCalories = settings.UserCalories;
            UserFat = settings.UserFat;
            UserProtein = settings.UserProtein;
            UserCarbohydrate = settings.UserCarbohydrate;


        }
    }
}
