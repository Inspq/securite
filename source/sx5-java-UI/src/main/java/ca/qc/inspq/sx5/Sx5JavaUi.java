package ca.qc.inspq.sx5;


import java.util.Enumeration;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI
public class Sx5JavaUi extends UI{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TextField textNom;
	private OptionGroup options;
	private TextArea resultat;
	@Autowired
	private Cookie[] cookies;
	
	@Override
	protected void init(VaadinRequest request) {
		Enumeration<String> headers = VaadinService.getCurrentRequest().getHeaderNames();
		while (headers.hasMoreElements()){ 
			String header = headers.nextElement();
			System.out.println("header: " + header + ": " + VaadinService.getCurrentRequest().getHeader(header));
		}
        VerticalLayout content = new VerticalLayout();
        //content.setSizeFull(); // Use entire window
        setContent(content);   // Attach to the UI

        // Add some component
        content.addComponent(new Label("Test de la sécurité sur des services web en Java",ContentMode.PREFORMATTED));
        textNom = new TextField("Nom");
        content.addComponent(textNom);
        options = new OptionGroup();
        //options.addItems("C# REST","C# SOAP","Java REST", "Java SOAP");
        options.addItems("C# REST","Java REST");
        content.addComponent(options);
        
        Button button = new Button("Shoot l'appel au service man!");

        button.addClickListener(new Button.ClickListener() {
            /**
			 * 
			 */
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
			javaBaseUrl = "http://qlapp00b.bicycle2.inspq.qc.ca";
		}
		String dotnetBaseUrl = System.getenv("DOTNET_REST_BASE_URL");
		if (dotnetBaseUrl == null){
			dotnetBaseUrl = "http://qlapp00b.bicycle2.inspq.qc.ca";
		}
		String url = "";
		if (options.getValue().equals("C# REST") || options.getValue().equals("Java REST")){
			if (options.getValue().equals("C# REST"))
				url = dotnetBaseUrl + "/api/rest/helloworld/" + textNom.getValue();
			else if (options.getValue().equals("Java REST"))
				url = javaBaseUrl + "/sx5-java-REST/java/rest/hello?nom=" + textNom.getValue();
			System.out.println("URL: " + url);
	        cookies = VaadinService.getCurrentRequest().getCookies();
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
