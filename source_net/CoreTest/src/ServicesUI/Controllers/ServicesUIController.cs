using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using ServicesUI.Models.ServicesUIViewModels;
using System.Net.Http;
using SoapReference;
using Microsoft.Extensions.Options;

// For more information on enabling MVC for empty projects, visit http://go.microsoft.com/fwlink/?LinkID=397860

namespace ServicesUI.Controllers
{
    public class ServicesUIController : Controller
    {
        private readonly IOptions<ServicesUiConfig> config;

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
            if (model.Services.Equals("C-R"))
            {
                //string retour = AppelerServiceRESTCSharp("http://localhost:50633", model.Nom).Result;
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
                string javaRestUrl = Environment.GetEnvironmentVariable("JAVA_REST_BASE_URL");
                if (javaRestUrl == null){
                    javaRestUrl = config.Value.JRHostname;
                }

                //string retour = AppelerServiceRESTJava("http://localhost:8080", model.Nom).Result;
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
                client.BaseAddress = new Uri(url);
                client.DefaultRequestHeaders.Accept.Clear();

                //HttpResponseMessage response = await client.GetAsync(String.Format("api/rest/helloworld/{0}", nom));
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
                    //HttpResponseMessage response = await client.GetAsync(String.Format("sx5-java-REST/java/rest/hello?nom={0}", nom));
                HttpResponseMessage response = await client.GetAsync(String.Format(config.Value.JRUri, nom));
                if (response.IsSuccessStatusCode)
                {
                    message = await response.Content.ReadAsStringAsync();
                }
            }
            return message;
        }
    }
}
