package luxe.chaos.choss.rdbs.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/11 22:52 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
@Configuration
@MapperScan(basePackages = ChossDataSourceConfig.PACKAGE,
        sqlSessionFactoryRef = ChossDataSourceConfig.SQL_SESSION_FACTORY_NAME)
public class ChossDataSourceConfig {

    static final String PACKAGE = "luxe.chaos.choss.**.mapper";
    static final String DATA_SOURCE_NAME = "chossDataSource";
    static final String SQL_SESSION_FACTORY_NAME = "chossSqlSessionFactory";

    @Bean(name = DATA_SOURCE_NAME)
    @Primary
    public DataSource chossDataSource() throws IOException {
        // 1. 获取 DataSource 相关信息
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        try (InputStream inputStream = resourceLoader
                .getResource("classpath:application.properties")
                .getInputStream()) {

            Properties prop = new Properties();
            prop.load(inputStream);
            Set<?> keys = prop.keySet();

            Properties dsProp = new Properties();
            for (Object key : keys) {
                if (key.toString().startsWith("datasource")) {
                    dsProp.put(key.toString().replace("datasource.", ""),
                            prop.get(key));
                }
            }

            // 2。 通过 HikariDataSourceFactory 生成 DataSource
            HikariDataSourceFactory factory = new HikariDataSourceFactory();
            factory.setProperties(dsProp);

            return factory.getDataSource();
        }

    }


    @Bean(name = SQL_SESSION_FACTORY_NAME)
    @Primary
    public SqlSessionFactory chossSqlSessionFactory(@Qualifier(DATA_SOURCE_NAME) DataSource dataSource) throws Exception {

        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();

        bean.setDataSource(dataSource);

        // 1. 读取 mybatis 相关配置。

        ResourceLoader loader = new DefaultResourceLoader();
        bean.setConfigLocation(loader.getResource("classpath:mybatis-config.xml"));
        // 2。 获取 sqlSessionFActory 实例
        bean.setSqlSessionFactoryBuilder(new SqlSessionFactoryBuilder());

        return bean.getObject();
    }


}
