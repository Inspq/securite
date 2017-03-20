using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace SoapService
{
    // REMARQUE : vous pouvez utiliser la commande Renommer du menu Refactoriser pour changer le nom de classe "Service1" à la fois dans le code et le fichier de configuration.
    public class SoapService : ISoapService
    {
        public string HelloWorld(string nom)
        {
            return String.Format("Bonjour C# SOAP {0}", nom);
        }
    }
}
