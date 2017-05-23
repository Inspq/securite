using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Authorization;
using scratch.Models.HomeViewModels;
using System.Net.Http;

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
            //ViewData["username"] = User.Identity.Name;
            //ViewData["token"] = HttpContext.Request.Cookies["iPlanetDirectoryPro"];
            PrivateViewModel model = new PrivateViewModel();
            model.Username = User.Identity.Name;
            model.Token = HttpContext.Request.Cookies["ca.qc.inspq.oidc.token"];
            return View("Private", model);
        }

        [HttpPost]
        public IActionResult Appeler(PrivateViewModel model)
        {
            string url = String.Empty;
            string request = String.Empty;
            if (model.AvecOuSansAgent == "avec")
            {
                url = Environment.GetEnvironmentVariable("DOTNET_REST_BASE_URL");
                if (url == null)
                    url = "http://sx5dotnetrest.bicycle2.inspq.qc.ca:8890/api/rest/";
                request = "helloworld/" + model.Username.Replace(" ", "");
            }
            else if (model.AvecOuSansAgent == "sans")
            {
                url = Environment.GetEnvironmentVariable("DOTNET_RESTOIDC_BASE_URL");
                if (url == null)
                    url = "http://localhost:5001/API/Home/";
                request = "auth";
            }
            string retour = AppelerServiceRESTCSharp(url, request, model.Token).Result;
            model.Retour = retour;
            return View("Private", model);
        }

        private async Task<string> AppelerServiceRESTCSharp(string url, string request, string token)
        {
            string message = "";
            using (HttpClient client = new HttpClient())
            {
                // Creer un client Http pour l'URL de base du service web.
                client.BaseAddress = new Uri(url);
                client.DefaultRequestHeaders.Accept.Clear();
                Console.WriteLine(url + request);
                // Ajouter le header Cookie contenant l'ensemble des cookies de la session actuelle dans le client Http.
                //client.DefaultRequestHeaders.Add("Cookie", cookiesString);
                client.DefaultRequestHeaders.Add("Authorization", "Bearer " + token);
                client.DefaultRequestHeaders.Add("Cookie",HttpContext.Request.Cookies["iPlanetDirectoryPro"]);

                // Appeler le service
                HttpResponseMessage response = await client.GetAsync(request);
                if (response.IsSuccessStatusCode)
                {
                    message = await response.Content.ReadAsStringAsync();
                }
            }
            return message;
        }
    }
}
