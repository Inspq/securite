package ca.qc.inspq.securite.rest;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ca.qc.inspq.securite.commun.Hello;

@RequestMapping(path = "/hello",
				produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@Service
public class HelloService {
    @RequestMapping(value = "",
    		method = RequestMethod.GET,
    		produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody Hello hello(@RequestParam(value="nom", defaultValue="World") String nom) {
    	return new Hello(nom);
    }
}
