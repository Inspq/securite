package ca.qc.inspq.securite.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import ca.qc.inspq.securite.session.Session;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class SessionController {
	@Autowired
    private Session session;

	@RequestMapping(path = "/attribute",
    		method = POST)
    protected String setSessionValue(@RequestParam("attributeName") String key,
            @RequestParam("attributeValue") String value) {
		session.setAttribute(key, value);
		return "views/attributes";
    }
}
