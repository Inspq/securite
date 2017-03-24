using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using ServicesUI.Models.ServicesUIViewModels;
using System.Net.Http;
using SoapReference;
using Microsoft.Extensions.Options;
using Microsoft.AspNetCore.Session;
using Microsoft.AspNetCore.Http;

// For more information on enabling MVC for empty projects, visit http://go.microsoft.com/fwlink/?LinkID=397860

namespace ServicesUI.Controllers
{
    public class ServicesUIController : Controller
    {
        private readonly IOptions<ServicesUiConfig> config;
        private String cookiesString;
        public ServicesUIController(IOptions<ServicesUiConfig> optionsAccessor)
        {
            this.config = optionsAccessor;
        }
        // GET: /<controller>/
        public IActionResult Index()
        {
            return View();
        }

        [HttpPost]
        public string Appeler(IndexViewModel model)
        {
            // Lire les cookies de la session et les stocker dans une chaine de caractere.
            foreach (var cookie in ControllerContext.HttpContext.Request.Cookies) {
				cookiesString = cookiesString + cookie.Key + "=" + cookie.Value +";" ;
			}
            Console.WriteLine("cookiesString: " + cookiesString);
            foreach(var header in ControllerContext.HttpContext.Request.Headers){
                Console.WriteLine("header: " + header.Key + ": " + header.Value);
            }
            if (model.Services.Equals("C-R"))
            {
                // Lire la valeur de la variable d'environnement contenant l'URL de base du service web Backend
                string dotnetRestUrl = Environment.GetEnvironmentVariable("DOTNET_REST_BASE_URL");
                if (dotnetRestUrl == null){
                    dotnetRestUrl = config.Value.CRHostname;
                }
                string retour = AppelerServiceRESTCSharp(dotnetRestUrl, model.Nom).Result;
                return retour;
            } else if (model.Services.Equals("C-S"))
            {
                string retour = AppelerServiceSOAPCSharp(model.Nom).Result;
                return retour;
            } else if (model.Services.Equals("J-R"))
            {
                // Lire la valeur de la variable d'environnement contenant l'URL de base du service web Backend
                string javaRestUrl = Environment.GetEnvironmentVariable("JAVA_REST_BASE_URL");
                if (javaRestUrl == null){
                    javaRestUrl = config.Value.JRHostname;
                }
                string retour = AppelerServiceRESTJava(javaRestUrl, model.Nom).Result;
                return retour;
            }
            return model.Services;
        }

        private async Task<string> AppelerServiceRESTCSharp(string url, string nom)
        {
            string message = "";
            using (HttpClient client = new HttpClient())
            {
                // Creer un client Http pour l'URL de base du service web.
                client.BaseAddress = new Uri(url);
                client.DefaultRequestHeaders.Accept.Clear();
                // Ajouter le header Cookie contenant l'ensemble des cookies de la session actuelle dans le client Http.
                client.DefaultRequestHeaders.Add("Cookie",cookiesString);

                // Appeler le service
                HttpResponseMessage response = await client.GetAsync(String.Format(config.Value.CRUri, nom));
                if (response.IsSuccessStatusCode)
                {
                    message = await response.Content.ReadAsStringAsync();
                }
            }
            return message;
        }

        private async Task<string> AppelerServiceSOAPCSharp(string nom)
        {
            string message = "";
            SoapServiceClient client = new SoapServiceClient();
            message = await client.HelloWorldAsync(nom);
            await client.CloseAsync();
            return message;
        }
        private async Task<string> AppelerServiceRESTJava(string url, string nom)
        {
            string message = "";
            using (HttpClient client = new HttpClient())
            {
                client.BaseAddress = new Uri(url);
                client.DefaultRequestHeaders.Accept.Clear();
                client.DefaultRequestHeaders.Add("Cookie", cookiesString);
                //HttpResponseMessage response = await client.GetAsync(String.Format("sx5-java-REST/java/rest/hello?nom={0}", nom));
                HttpResponseMessage response = await client.GetAsync(String.Format(config.Value.JRUri, nom));
                if (response.IsSuccessStatusCode)
                {
                    message = await response.Content.ReadAsStringAsync();
                }
            }
            return message;
        }

        private static HttpClient GetClient(HttpClient client)
        {
            return client;
        }
    }
}
