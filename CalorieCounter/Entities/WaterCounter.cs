using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace CalorieCounter.Entities
{
    public class WaterCounter
    {
        [Key]
        public long Id { get; set; }
        [ForeignKey("User")]
        public long UserId { get; set; }
        public virtual DbUser User { get; set; }
        public int WaterVolume { get; set; }
        public DateTime DateOfDrink { get; set; }
    }
}
