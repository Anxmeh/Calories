using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CalorieCounter.Models
{
    public class UserLoginViewModel
    {
        public string Email { get; set; }
        public string Password { get; set; }
    }

    public class UserRegisterViewModel
    {
        public string Email { get; set; }
        public string ImageBase64 { get; set; }
        public string Password { get; set; }
    }


    public class GetUserViewModel
    {
        public string Email { get; set; }
        public string Name { get; set; }
    }
    public class GetAllUsersViewModel
    {
        public ICollection<GetUserViewModel> AllUsers { get; set; }
    }
}
