using Microsoft.AspNetCore.Identity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CalorieCounter.Entities
{
    public class DbUser : IdentityUser<long>
    {
        public virtual ICollection<DbUserRole> UserRoles { get; set; }
        public virtual UserProfile UserProfile { get; set; }
    }
}
