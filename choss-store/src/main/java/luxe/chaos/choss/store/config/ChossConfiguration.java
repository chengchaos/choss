package luxe.chaos.choss.store.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@Component
public class ChossConfiguration {

    private Map<String, Object> props = new HashMap<>();

    public ChossConfiguration() {
        super();
        Properties properties = System.getProperties();
        properties.setProperty("HADOOP_USER_NAME", "chengchao");

//        props.put("hadoop.config.dir", "");
        props.put("hadoop.hdfs.uri", "hdfs://192.168.0.111:8020");

    }

    public String getString(String name) {
        Object value = this.props.get(name);

        if (value == null) {
            throw new NullPointerException("get properties name '" + name + "', but value is null");
        }

        return value.toString();
    }
}
