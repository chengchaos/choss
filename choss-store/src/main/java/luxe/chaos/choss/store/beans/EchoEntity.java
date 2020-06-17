package luxe.chaos.choss.store.beans;

import java.util.HashMap;
import java.util.Map;

public class EchoEntity {

    private final Map<String, String> header;
    private final Map<String, String> params;

    public EchoEntity() {
        this.header = new HashMap<>();
        this.params = new HashMap<>();
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public EchoEntity addHeader(String key, String value) {
        this.header.put(key, value);
        return this;
    }

    public EchoEntity addParams(String key, String value) {
        this.params.put(key, value);
        return this;
    }
}
