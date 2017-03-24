package ca.qc.inspq.sx5.service;

import ca.qc.inspq.sx5.service.soap.HelloRequest;
import ca.qc.inspq.sx5.service.soap.HelloResponse;

import org.springframework.util.StringUtils;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class JavaSoap {
    private static final String NAMESPACE_URI = "http://sx5.inspq.qc.ca/service/soap";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "helloRequest")
    @ResponsePayload
    public HelloResponse hello(@RequestPayload HelloRequest requete) {
        HelloResponse reponse = new HelloResponse();
        String nom = requete.getNom();
        if (StringUtils.isEmpty(nom)) {
            nom = "World";
        }
        reponse.setHello(String.format("Hello %s!", nom));

        return reponse;
    }
}
