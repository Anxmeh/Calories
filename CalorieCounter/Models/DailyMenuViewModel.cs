using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CalorieCounter.Models
{
    public class DailyMenuViewModel
    {
        public long UserId { get; set; }
        public double ProductWeight { get; set; }
        public string ProductName { get; set; }
        public double ProductProtein { get; set; }
        public double ProductFat{ get; set; }
        public double ProductCarbohydrate { get; set; }
        public double ProductCalories { get; set; }
        public long ProductId { get; set; }
        public DateTime DateOfMeal { get; set; }
        //public static double TotalCalories { get; set; }
        //public static double TotalWeight { get; set; }


    }

    public class CalculateDailyViewModel
    {
        public double TotalCalories { get; set; }
        public double TotalWeight { get; set; }
    }

    public class AddProductDailyViewModel
    {
     //  public long ProductId { get; set; }
        public string ProductName { get; set; }
        public double ProductProtein { get; set; }
        public double ProductFat { get; set; }
        public double ProductCarbohydrate { get; set; }
        public double ProductCalories { get; set; }
        public double ProductWeight { get; set; }
        public DateTime DateOfMeal { get; set; }

    }

    public class RemoveProductDailyViewModel
    {
        public long ProductId { get; set; }
       public DateTime DateOfMeal { get; set; }

    }
}
