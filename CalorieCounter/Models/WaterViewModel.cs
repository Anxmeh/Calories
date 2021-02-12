using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CalorieCounter.Models
{
    public class WaterViewModel
    {
        public long UserId { get; set; }
        public int WaterVolume { get; set; }
        public DateTime DateOfDrink { get; set; }
    }

    public class AddWaterViewModel
    {
       public int WaterVolume { get; set; }
        public DateTime DateOfDrink { get; set; }
    }

}
