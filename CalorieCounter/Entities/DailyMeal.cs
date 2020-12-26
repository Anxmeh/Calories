using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace CalorieCounter.Entities
{
    public class DailyMeal
    {
        [Key]
        public long Id { get; set; }
        public DateTime DateOfMeal { get; set; }
        public double ProductWeight { get; set; }

        // [ForeignKey("User")]
        public long UserId { get; set; } // переобила
                                         //  public virtual DbUser User { get; set; }


        [ForeignKey("Product")]
        public long ProductId { get; set; }
        public Product Product { get; set; } //virtual????
    }
}
