package ca.qc.inspq.securite.rest;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import ca.qc.inspq.securite.session.Session;

@RequestMapping("/session")
@RestController
@Scope(WebApplicationContext.SCOPE_SESSION)
public class SessionService {
    @Autowired
    private Session session;

    @RequestMapping(path = "/attribute",
    		method = POST)
    protected void setSessionValue(@RequestParam("attributeName") String key,
            @RequestParam("attributeValue") String value) {
        session.setAttribute(key, value);
    }

    @RequestMapping(path = "/attribute",
    		method = GET)
    protected String getSessionValue(@RequestParam("attributeName") String key) {
        return session.getAttribute(key);
    }

    @RequestMapping(path = "/attribute/all",
    		method = GET)
    protected Map<String, String> getAllSessionValues() {
        return session.getAttributes();
    }
}
