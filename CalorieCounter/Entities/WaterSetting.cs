using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace CalorieCounter.Entities
{
    public class WaterSetting
    {
        [Key]
        public long Id { get; set; }
        [ForeignKey("User")]
        public long UserId { get; set; }
        public virtual DbUser User { get; set; }
        public int BeginHour { get; set; }
        public int EndHour { get; set; }
        public int BeginMinute{ get; set; }
        public int EndMinute { get; set; }

    }
}
