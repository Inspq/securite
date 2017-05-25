package ca.qc.inspq.securite.controller;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import ca.qc.inspq.securite.commun.Hello;
import ca.qc.inspq.securite.session.Session;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class HelloController {
	private static final String ATTRIBUT_HELLO_RESULT = "HelloResult";

	@Value("${sx5.java.base.url}")
	private String urlJava;

	@Value("${sx5.dotnet.base.url}")
	private String urlDotNet;

	@Autowired
	private OAuth2RestTemplate restTemplate;

	@Autowired
	private Session session;

	@PostConstruct
	private void init() {
		if (session.getAttribute(ATTRIBUT_HELLO_RESULT) == null) {
			session.setAttribute(ATTRIBUT_HELLO_RESULT, "");
		}
	}

	@RequestMapping(path = "/hello",
			method = RequestMethod.GET)
	public String hello(@RequestParam("serveur") String serveur, @RequestParam("nom") String nom) throws Exception {
		String helloUrl = String.format("%s%s", 
				"java".equalsIgnoreCase(serveur) ? urlJava : urlDotNet,
				nom);
		
		String result = "";
		try {
			Hello hello = restTemplate.getForEntity(helloUrl, Hello.class).getBody();
			result = hello.toString();
		} catch (Exception e) {
			result = e.getMessage();
			e.printStackTrace();
		} finally {
			session.setAttribute(ATTRIBUT_HELLO_RESULT,
					String.format("%s[Call]: %s [Result]: %s\n", session.getAttribute(ATTRIBUT_HELLO_RESULT), helloUrl, result));
			
		}
		return "views/home";
    }
}
