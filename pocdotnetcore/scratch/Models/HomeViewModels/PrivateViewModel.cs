using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace scratch.Models.HomeViewModels
{
    public class PrivateViewModel
    {
        public string Username { get; set; }
        public string Token { get; set; }

        public string AvecOuSansAgent { get; set; }
        public string Retour { get; set; }
    }
}
