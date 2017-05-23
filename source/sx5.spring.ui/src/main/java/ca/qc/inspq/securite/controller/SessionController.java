package ca.qc.inspq.securite.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;

import ca.qc.inspq.securite.session.Session;

@RequestMapping("/session")
@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class SessionController {
	@Value("${sx5.java.base.url}")
	private String url;

	@Autowired
    private Session session;

	@Autowired
	private OAuth2RestTemplate restTemplate;

	@RequestMapping(path = "/attribute",
    		method = POST)
	@ResponseBody
    protected void setSessionValue(@RequestParam("attributeName") String key,
            @RequestParam("attributeValue") String value) {
		String setSessionValueUrl = String.format("%s/attribute", url);

		HttpEntity<String> requestEntity = new HttpEntity<String>(String.format("%s&%s", key, value));
	    restTemplate.postForObject(setSessionValueUrl, requestEntity, Void.class);
    }

    @RequestMapping(path = "/attribute",
    		method = GET)
	@ResponseBody
    protected String getSessionValue(@RequestParam("attributeName") String key) {
		String setSessionValueUrl = String.format("%s/attribute", url);
		Map<String, String> params = new HashMap<String, String>();
		params.put("attributeName", key);

	    return restTemplate.getForObject(setSessionValueUrl, String.class, params);
    }

    @SuppressWarnings("unchecked")
	@RequestMapping(path = "/attribute/all",
    		method = GET)
	@ResponseBody
    protected Map<String, String> getAllSessionValues() {
		String setSessionValueUrl = String.format("%s/attribute/all", url);

	    return restTemplate.getForObject(setSessionValueUrl, Map.class);
    }
}
