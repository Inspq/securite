package ca.qc.inspq.securite.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import ca.qc.inspq.securite.commun.Hello;
import ca.qc.inspq.securite.session.Session;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class HelloController {
	private static final DateFormat df = new SimpleDateFormat("hh:mm:ss");

	@Value("${sx5.java.oidc.base.url}")
	private String urlJava;

	@Value("${sx5.dotnet.oidc.base.url}")
	private String urlDotNet;

	@Value("${sx5.java.base.url}")
	private String urlJavaAgent;

	@Value("${sx5.dotnet.base.url}")
	private String urlDotNetAgent;
	
	@Autowired
	private OAuth2RestTemplate restTemplate;

	@Autowired
	private Session session;
	
	@RequestMapping(path = "/hello",
			method = RequestMethod.GET)
	public String hello(@RequestParam("serveur") String serveur, @RequestParam("nom") String nom, @RequestParam("type") String type, @RequestHeader("Cookie") String cookies) throws Exception {
		String helloUrl = "";
		if (type.equals("agent"))
			helloUrl = String.format("%s%s", 
				"java".equalsIgnoreCase(serveur) ? urlJavaAgent : urlDotNetAgent,
				nom);
		else
			helloUrl = String.format("%s%s", 
				"java".equalsIgnoreCase(serveur) ? urlJava : urlDotNet,
				nom);
		
		String result = "";
		Hello hello;
		
		try {
			if (type.equals("agent")){
		        // Creer le header contenant les cookies
		        HttpHeaders headers = new HttpHeaders();
				System.out.println("cookiesString: " + cookies);
				headers.set("Cookie", cookies);
				// Creer un HttpEntity contenant le header
				HttpEntity<String> requestEntity = new HttpEntity<String>(null, headers);
				//Hello hello = restTemplate.getForObject(url, Hello.class);
				// Appeler le service REST avec le header contenant les cookies pour faire le SSO
				ResponseEntity<Hello> respEntity = restTemplate.exchange(helloUrl, HttpMethod.GET, requestEntity, Hello.class);
				hello = respEntity.getBody();

			} else {
				hello = restTemplate.getForEntity(helloUrl, Hello.class).getBody();
			}
			//result = hello.toString();
			result = hello.getHello();
		} catch (Exception e) {
			result = e.getMessage();
			e.printStackTrace();
		} finally {
			session.setAttribute(String.format("[%s] %s", df.format(System.currentTimeMillis()), helloUrl), result);
			
		}
		return "views/home";
    }
}
