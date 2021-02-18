using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CalorieCounter.Models
{
    public class WaterSettingsViewModel
    {
        public int BeginHour { get; set; }
        public int EndHour { get; set; }
        public int BeginMinute { get; set; }
        public int EndMinute { get; set; }
    }

    public class SetWaterSettingsViewModel
    {
        public int Hour { get; set; }
       public int Minute { get; set; }
        
    }
}
