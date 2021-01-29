using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace CalorieCounter.Entities
{
    public class Vitamin
    {
        [Key]
        public long Id { get; set; }
        [Required, StringLength(255)]
        public string VitaminName { get; set; }
       
    }
}
