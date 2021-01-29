using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CalorieCounter.Models
{
    public class GetVitaminsViewModel
    {
        public long Id { get; set; }
        public string VitaminName { get; set; }
       
    }



    public class AddMyVitaminsViewModel
    {

        public long VitaminId { get; set; }
        public string VitaminName { get; set; }
        public int Amount { get; set; }

    }

    public class VitaminSettingsViewModel
    {
        public long VitaminId { get; set; }
        public long UserId { get; set; }
        public int Amount { get; set; }

    }



    public class VitaminsViewModel
    {
        public long Id { get; set; }
              public string VitaminName { get; set; }
    }
    

    public class MyVitaminsViewModel
    {
        public long UserId { get; set; }
        public long VitaminId { get; set; }
        public int Amount { get; set; }

    }
    public class ShowMyVitaminsViewModel
    {
           public string VitaminName { get; set; }
        public int Amount { get; set; }

    }

    
}
