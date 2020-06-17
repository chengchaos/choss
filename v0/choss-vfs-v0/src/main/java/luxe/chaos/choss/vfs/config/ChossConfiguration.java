package luxe.chaos.choss.vfs.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/13 14:18 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
public class ChossConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChossConfiguration.class);

    private static final ChossConfiguration configuration = new ChossConfiguration();
    private static final Properties properties = new Properties();

    static {
        PathMatchingResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resourceLoader.getResources("classpath:*.properties");
            Stream.of(resources).forEach(ChossConfiguration::readPropFromResource);
        } catch (IOException e) {
            LOGGER.error(StringUtils.EMPTY, e);
        }
    }

    private static void readPropFromResource(Resource resource) {
        try (InputStream input = resource.getInputStream()) {
            Properties props = new Properties();
            props.load(input);
            properties.putAll(props);
        } catch (IOException e) {
            LOGGER.error(StringUtils.EMPTY, e);
        }
    }

    private ChossConfiguration() {
        super();
    }

    public static ChossConfiguration getConfiguration() {
        return configuration;
    }


    public String getString(String key) {
        return properties.getProperty(key);
    }

    public int getInt(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }

    public long getLong(String key) {
        return Long.parseLong(properties.getProperty(key));
    }
}
