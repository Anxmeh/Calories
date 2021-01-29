using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace CalorieCounter.Entities
{
    public class UserVitamins
    {
        [Key]
        public long Id { get; set; }
        [ForeignKey("User")]
        public long UserId { get; set; }
        public virtual DbUser User { get; set; }
        [ForeignKey("Vitamin")]
        public long VitaminId { get; set; }
        public virtual Vitamin Vitamin { get; set; }
        public int Amount { get; set; }
        public DateTime DateOfVitamin { get; set; }
        public bool IsTaken { get; set; }


    }
}
