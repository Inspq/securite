using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Formatters;
using TestRestService.Models;

namespace TestRestService.Controllers
{
    [Route("api/[controller]")]
    public class RestController : Controller
    {
        [HttpGet("helloworld/{nom}")]
        public Hello HelloWorld(string nom)
        {
            Hello hello = new Hello();
            hello.hello = nom;
            //return String.Format("Bonjour C# REST {0}", nom);
            return hello;
        }

        [HttpPost("helloworld")]
        public string HandShake([FromBody] Info body)
        {
            return String.Format("Handshake {0}", body.Texte);
        }
    }
}
