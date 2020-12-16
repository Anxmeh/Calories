using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace CalorieCounter.Entities
{
    public class Dish
    {
        [Key]
        public long Id { get; set; }
        [Required, StringLength(255)]
        public string ProductName { get; set; }
        public double ProductProtein { get; set; }
        public double ProductFat { get; set; }
        public double ProductCarbohydrate { get; set; }
        public double ProductCalories { get; set; }
        public double ProductWeight { get; set; }
    }
}
