package ca.qc.inspq.sx5;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import hello.wsdl.HelloRequest;
import hello.wsdl.HelloResponse;

public class HelloJavaSoapClient extends WebServiceGatewaySupport {


	public HelloResponse getHello(String nom) {

		HelloRequest request = new HelloRequest();
		request.setNom(nom);

		String baseUrl = System.getenv("JAVA_SOAP_BASE_URL");
		if (baseUrl == null){
			baseUrl = "http://qlapp00b.bicycle2.inspq.qc.ca";
		}
		HelloResponse response = (HelloResponse) getWebServiceTemplate()
				.marshalSendAndReceive(
						baseUrl + "/sx5-java-SOAP/java/soap",
						request);

		return response;
	}

}
