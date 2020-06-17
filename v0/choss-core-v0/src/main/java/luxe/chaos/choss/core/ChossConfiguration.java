package luxe.chaos.choss.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * <p>
 * <strong>
 * 获取所有的配置文件的所有属性
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/11 23:37 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
public class ChossConfiguration {


    private static final Logger LOGGER = LoggerFactory.getLogger(ChossConfiguration.class);
    private static final ChossConfiguration INSTANCE = new ChossConfiguration();
    private static final Properties properties;


    static {
        PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        properties = new Properties();

        try {
            Resource[] resources = resourcePatternResolver
                    .getResources("classpath:*.properties");
            for (Resource r : resources) {
                Properties prop = new Properties();
                try (InputStream is = r.getInputStream()) {
                    prop.load(is);
                    properties.putAll(prop);
                } catch (IOException e) {
                    LOGGER.error("", e);
                }
            }
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }

    private ChossConfiguration() {
        super();
    }


    public static ChossConfiguration getInstance() {
        return INSTANCE;
    }

    public String getString(String key) {
        return properties.get(key).toString();
    }

    public int getInt(String key) {
        return  Integer.parseInt(getString(key), 10);
    }
}
