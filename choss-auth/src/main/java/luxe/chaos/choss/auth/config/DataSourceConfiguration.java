package luxe.chaos.choss.auth.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * 1 ) 配置数据源
 * <p>
 * 2 ） 配置 SqlSessionFactory 对象
 * SqlSession 是 MyBatis 提供的与数据库交互的接口。
 * SqlSession 的创建依赖于  SqlSessionFactory
 * <p>
 * 3 ) 配置 SqlSessionTemplate 对象
 * <p>
 * 4 ） 通过 Mapper Scan 注解扫描 Mapper 接口
 */
@Configuration
@MapperScan(basePackages = DataSourceConfiguration.MAPPER_BASE_PACKAGE,
sqlSessionFactoryRef = "sqlSessionFactory")
@EnableTransactionManagement
public class DataSourceConfiguration {

    private final static Logger LOGGER = LoggerFactory.getLogger(DataSourceConfiguration.class);

    static final String MAPPER_BASE_PACKAGE = "luxe.chaos.choss.auth.dao";

    public static class HikariDataSourceFactory extends UnpooledDataSourceFactory {
        public HikariDataSourceFactory() {
            this.dataSource = new HikariDataSource();
        }
    }

    @Primary
    @Bean(name = "dataSource")
    public DataSource dataSource() {
        ResourceLoader loader = new DefaultResourceLoader();
        try (InputStream inputStream = loader.getResource("classpath:data-source.properties")
                .getInputStream()) {

            Properties targetProps = new Properties();
            final Properties originProps = new Properties();
            originProps.load(inputStream);

            final Enumeration<Object> keys = originProps.keys();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement().toString();
                targetProps.put(key.replace("datasource.", ""), originProps.getProperty(key));
            }

            HikariDataSourceFactory factory = new HikariDataSourceFactory();
            factory.setProperties(targetProps);
            return factory.getDataSource();
        } catch (IOException ioex) {
            throw new IllegalStateException("Reading properties file exception ", ioex);
        }
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) {
        // 通过 MyBatis Spring 提供的 SqlSessionFactoryBean 对象
        // 创建 MyBatis 的 SqlSessionFactory 对象。
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);

        // 1: 读取 mybatis 的相关配置
        ResourceLoader loader = new DefaultResourceLoader();
        bean.setConfigLocation(loader.getResource("classpath:mybatis-config.xml"));
        // 2: 获取 sqlSessionFactory 实例
        bean.setSqlSessionFactoryBuilder(new SqlSessionFactoryBuilder());

//        Properties props = new Properties();
//        // 分页合理化，true开启，如果分页参数不合理会自动修正。
//        // 默认false不启用
//        props.setProperty("reasonable", "false");
//        // 是否支持接口参数来传递分页参数，默认false
//        props.setProperty("supportMethodsArguments", "true");
//        props.setProperty("returnPageInfo", "check");
//        props.setProperty("params", "count=countSql");

//        PageInterceptor pageInterceptor = new PageInterceptor();
//        pageInterceptor.setProperties(props);
//        bean.setPlugins(new Interceptor[]{pageInterceptor});


        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            bean.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));
            return bean.getObject();
        } catch (Exception e) {
            throw new IllegalStateException("Create SqlSessionFactory get Exception ", e);
        }

    }


    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {

        return new SqlSessionTemplate(sqlSessionFactory);
    }
//
//    @Bean
//    //@Override
//    public PlatformTransactionManager annotationDrivenTransactionManager() {
//        return new DataSourceTransactionManager(dataSource);
//    }
}
