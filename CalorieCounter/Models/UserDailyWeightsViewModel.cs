using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CalorieCounter.Models
{
    public class UserDailyWeightsViewModel
    {
        public long Id { get; set; }
        public long UserId { get; set; }
        public DateTime DateOfWeight { get; set; }
        public double Weight { get; set; }
    }

    public class AddUserDailyWeightsViewModel
    {
        public DateTime DateOfWeight { get; set; }
        public double Weight { get; set; }
    }
}
