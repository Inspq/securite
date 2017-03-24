package ca.qc.inspq.sx5;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class HelloJavaSoapConfiguration {
	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("hello.wsdl");
		return marshaller;
	}

	@Bean
	public HelloJavaSoapClient helloJavaSoapClient(Jaxb2Marshaller marshaller) {
		HelloJavaSoapClient client = new HelloJavaSoapClient();
		String baseUrl = System.getenv("JAVA_SOAP_BASE_URL");
		if (baseUrl == null){
			baseUrl = "http://qlapp00b.bicycle2.inspq.qc.ca";
		}

		client.setDefaultUri(baseUrl + "/sx5-java-SOAP/java/soap");
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}
}
