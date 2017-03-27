using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Authorization;

// For more information on enabling MVC for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace scratch.Controllers
{
    public class HomeController : Controller
    {
        // GET: /<controller>/
        public IActionResult Index()
        {
            return View();
        }

        [Authorize]
        public IActionResult Private()
        {
            ViewData["username"] = User.Identity.Name;
            ViewData["token"] = HttpContext.Request.Cookies["iPlanetDirectoryPro"];
            return View();
        }
    }
}
