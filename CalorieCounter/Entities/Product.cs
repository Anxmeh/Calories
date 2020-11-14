using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace CalorieCounter.Entities
{
    public class Product
    {
        [Key]
        public long Id { get; set; }
        [Required, StringLength(255)]
        public string Name { get; set; }
        public double Protein { get; set; }
        public double Fat { get; set; }
        public double Carbohydrate { get; set; }
        public double Calories { get; set; }
    }
}
