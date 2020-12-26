using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CalorieCounter.Models
{
    public class ProductViewModel
    {
        public long Id { get; set; }
        public string Name { get; set; }
        public double Protein { get; set; }
        public double Fat { get; set; }
        public double Carbohydrate { get; set; }
        public double Calories { get; set; }
    }

    public class AddProductViewModel
    {
        public string Name { get; set; }
        public double Protein { get; set; }
        public double Fat { get; set; }
        public double Carbohydrate { get; set; }
        public double Calories { get; set; }
    }

    public class RemoveProductViewModel
    {
        public string Name { get; set; }
        public double Protein { get; set; }
        public double Fat { get; set; }
        public double Carbohydrate { get; set; }
        public double Calories { get; set; }
    }

    public class TesttViewModel
    {
        public long Id { get; set; }
        public long UserId { get; set; }
        public DateTime Date { get; set; }
        public double ProductWeight { get; set; }
        public long ProductId { get; set; }
    }

    public class TestJoinViewModel
    {
          public long UserId { get; set; }
               public double ProductWeight { get; set; }
        public string ProductName { get; set; }
        public double ProductProtein { get; set; }

    }
}
