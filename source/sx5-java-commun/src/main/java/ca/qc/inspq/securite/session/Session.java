package ca.qc.inspq.securite.session;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Session {
    private Map<String, String> attributes = new HashMap<String, String>();

    public void setAttribute(String key, String value) {
        attributes.put(key, value);
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }

    public Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }
}
