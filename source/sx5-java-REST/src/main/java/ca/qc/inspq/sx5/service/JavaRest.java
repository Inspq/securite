package ca.qc.inspq.sx5.service;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ca.qc.inspq.securite.commun.Hello;

@RequestMapping(path = "/java/rest",
				method=RequestMethod.GET,
				produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class JavaRest {
    @RequestMapping(path = "/hello", method = RequestMethod.GET, produces="application/json")
    //public String hello(@RequestParam(value="nom",defaultValue="World") String nom) {
    public @ResponseBody Hello hello(@RequestParam(value="nom",defaultValue="World") String nom) {
    	Hello hello = new Hello(nom);
    	return hello;
        //return String.format("Hello %s!", nom);
    }
}
