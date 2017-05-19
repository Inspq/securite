package ca.qc.inspq.sx5.ui;

import java.util.Enumeration;

import javax.servlet.http.Cookie;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import ca.qc.inspq.securite.commun.Hello;

import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI
public class Sx5JavaUi extends UI{
	private static final long serialVersionUID = 1L;
	private TextField textIdentification;

	private TextField textNom;
	private OptionGroup options;
	private TextArea resultat;
	
	@Override
	protected void init(VaadinRequest request) {
		String mail = "";
		String nomCompet = "";
		String userid = "";
		Enumeration<String> headers = VaadinService.getCurrentRequest().getHeaderNames();
		while (headers.hasMoreElements()){ 
			String header = headers.nextElement();
			System.out.println("header: " + header + ": " + VaadinService.getCurrentRequest().getHeader(header));
			if (header.equals("mail"))
				mail = VaadinService.getCurrentRequest().getHeader(header);
			else if (header.equals("nomcomplet"))
				nomCompet = VaadinService.getCurrentRequest().getHeader(header);
			else if (header.equals("iv-user"))
				userid = VaadinService.getCurrentRequest().getHeader(header);
		}
        VerticalLayout content = new VerticalLayout();
        //content.setSizeFull(); // Use entire window
        setContent(content);   // Attach to the UI

        // Add some component
        content.addComponent(new Label("Test de la sécurité sur des services web en Java",ContentMode.PREFORMATTED));
        content.addComponent(new Label("Bonjour " + nomCompet ,ContentMode.PREFORMATTED));
        content.addComponent(new Label("courriel: " + mail ,ContentMode.PREFORMATTED));
        content.addComponent(new Label("userid: " + userid ,ContentMode.PREFORMATTED));
        textNom = new TextField("Nom");
        content.addComponent(textNom);
        options = new OptionGroup();
        //options.addItems("C# REST","C# SOAP","Java REST", "Java SOAP");
        options.addItems("C# REST","Java REST");
        content.addComponent(options);
        
        Button button = new Button("Shoot l'appel au service man!");

        button.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
                appelerService();
            }
        });
        content.addComponent(button);
        resultat = new TextArea("Résultat");
        content.addComponent(resultat);
        // Layout inside layout
        //HorizontalLayout hor = new HorizontalLayout();
        //hor.setSizeFull(); // Use all available space

	}
	
	private void appelerService(){
		RestTemplate restTemplate = new RestTemplate();
		// Lire les variables d'environnement contenant les adresses des services backend.
		String javaBaseUrl = System.getenv("JAVA_REST_BASE_URL");
		if (javaBaseUrl == null){
			javaBaseUrl = "http://sx5javarest.bicycle2.inspq.qc.ca:8888";
		}
		String dotnetBaseUrl = System.getenv("DOTNET_REST_BASE_URL");
		if (dotnetBaseUrl == null){
			dotnetBaseUrl = "http://sx5dotnetrest.bicycle2.inspq.qc.ca:8890";
		}
		String url = "";
		if (options.getValue().equals("C# REST") || options.getValue().equals("Java REST")){
			if (options.getValue().equals("C# REST"))
				url = dotnetBaseUrl + "/api/rest/helloworld/" + textNom.getValue();
			else if (options.getValue().equals("Java REST"))
				url = javaBaseUrl + "/sx5-java-REST/java/rest/hello?nom=" + textNom.getValue();
			System.out.println("URL: " + url);
			Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
	        String cookiesString = "";
			// Lire les cookies de la session
	        for (Cookie cookie : cookies) {
				System.out.println(cookie.getName() + " " + cookie.getValue());
				cookiesString = cookiesString + cookie.getName() + "=" + cookie.getValue() +";" ;
			}
	        // Creer le header contenant les cookies
	        HttpHeaders headers = new HttpHeaders();
			System.out.println("cookiesString: " + cookiesString);
			headers.set("Cookie", cookiesString);
			// Creer un HttpEntity contenant le header
			HttpEntity<String> requestEntity = new HttpEntity<String>(null, headers);
			//Hello hello = restTemplate.getForObject(url, Hello.class);
			// Appeler le service REST avec le header contenant les cookies pour faire le SSO
			ResponseEntity<Hello> respEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Hello.class);
			Hello hello = respEntity.getBody();
			resultat.setValue("Bonjour " + hello.getHello() + " du service " + options.getValue());
		} else {
			resultat.setValue("Cette methode n'a pas été implémentée");
		}
	}
}
