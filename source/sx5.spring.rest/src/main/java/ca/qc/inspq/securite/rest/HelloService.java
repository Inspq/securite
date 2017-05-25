package ca.qc.inspq.securite.rest;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ca.qc.inspq.securite.commun.Hello;

@RequestMapping(path = "/helloworld",
				produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@Service
public class HelloService {
    @RequestMapping(value = "/{nom}",
    		method = RequestMethod.GET,
    		produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody Hello hello(@PathVariable(value="nom") String nom) {
    	return new Hello(nom);
    }

    @RequestMapping(value = {"", "/"},
    		method = RequestMethod.GET,
    		produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody Hello hello() {
    	return hello("World");
    }
}
