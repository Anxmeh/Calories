using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CalorieCounter.Models
{
    public class DishViewModel
    {
        public long Id { get; set; }
        public string ProductName { get; set; }
        public double ProductProtein { get; set; }
        public double ProductFat { get; set; }
        public double ProductCarbohydrate { get; set; }
        public double ProductCalories { get; set; }
       public double ProductWeight { get; set; }
    }

    public class AddDishViewModel
    {
        public string ProductName { get; set; }
        public double ProductProtein { get; set; }
        public double ProductFat { get; set; }
        public double ProductCarbohydrate { get; set; }
        public double ProductCalories { get; set; }
        public double ProductWeight { get; set; }
    }

    public class RemoveDishViewModel
    {
        public string ProductName { get; set; }
        public double ProductProtein { get; set; }
        public double ProductFat { get; set; }
        public double ProductCarbohydrate { get; set; }
        public double ProductCalories { get; set; }
        public double ProductWeight { get; set; }
    }

    public class ResultDishViewModel
    {
        public double DishCalories { get; set; }
        public double DishWeight { get; set; }
    }
}
