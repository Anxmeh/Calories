using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace CalorieCounter.Entities
{
    public class UserSettings
    {
        [Key, ForeignKey("User")]
        public long Id { get; set; }
        public virtual DbUser User { get; set; }
        public int Age { get; set; }
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

        public UserSettings()
        {

        }

    }
}
