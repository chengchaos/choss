

### 1，引入 MyBatis 依赖


```xml
		<!-- mybatis and mysql begin -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-jdbc</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jdbc</artifactId>
		</dependency>
		<dependency>
			<groupId>org.mybatis.spring.boot</groupId>
			<artifactId>mybatis-spring-boot-starter</artifactId>
			<version>2.1.3</version>
		</dependency>
		<dependency>
			<groupId>com.zaxxer</groupId>
			<artifactId>HikariCP</artifactId>
			<version>2.7.9</version>
		</dependency>
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<scope>runtime</scope>
		</dependency>
		<!-- mybatis and mysql end -->
```

### 2, 添加 mybatis-config.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
<!--    <properties resource="db.properties"></properties>-->
<!--    <environments default="development">-->
<!--        <environment id="development">-->
<!--            <transactionManager type="JDBC"></transactionManager>-->
<!--            <dataSource type="POOLED">-->
<!--                <property name="driver" value="${jdbc.driver}"></property>-->
<!--                <property name="url" value="${jdbc.url}"></property>-->
<!--                <property name="username" value="${jdbc.username}"></property>-->
<!--                <property name="password" value="${jdbc.password}"></property>-->
<!--            </dataSource>-->
<!--        </environment>-->
<!--    </environments>-->
    <!--加载映射文件-->
<!--    <mappers>-->
<!--        <mapper resource="mapper/adminMap.xml"></mapper>-->
<!--        <mapper resource="mapper/mapper.xml"></mapper>-->
<!--    </mappers>-->


    <!-- settings是 MyBatis 中极为重要的调整设置，它们会改变 MyBatis 的运行时行为。 -->
    <settings>
        <!-- 该配置影响的所有映射器中配置的缓存的全局开关。默认值true -->
        <setting name="cacheEnabled" value="true"/>
        <!--延迟加载的全局开关。当开启时，所有关联对象都会延迟加载。
        特定关联关系中可通过设置fetchType属性来覆盖该项的开关状态。默认值false
        -->
        <setting name="lazyLoadingEnabled" value="true"/>
        <!-- 是否允许单一语句返回多结果集（需要兼容驱动）。 默认值true -->
        <setting name="multipleResultSetsEnabled" value="true"/>
        <!-- 使用列标签代替列名。不同的驱动在这方面会有不同的表现，
        具体可参考相关驱动文档或通过测试这两种不同的模式来观察所用驱动的结果。
        默认值true
        -->
        <setting name="useColumnLabel" value="true"/>

        <!--
        允许 JDBC 支持自动生成主键，需要驱动兼容。
        如果设置为 true 则这个设置强制使用自动生成主键，
        尽管一些驱动不能兼容但仍可正常工作（比如 Derby）。
        默认值false
        -->
        <setting name="useGeneratedKeys" value="false"/>

        <!--
        指定 MyBatis 应如何自动映射列到字段或属性。
        NONE 表示取消自动映射；PARTIAL 只会自动映射没有定义嵌套结果集映射的结果集。
        FULL 会自动映射任意复杂的结果集（无论是否嵌套）。
        -->
        <!-- 默认值PARTIAL -->
        <setting name="autoMappingBehavior" value="PARTIAL"/>

        <setting name="autoMappingUnknownColumnBehavior" value="WARNING"/>

        <!--  配置默认的执行器。SIMPLE 就是普通的执行器；
        REUSE 执行器会重用预处理语句（prepared statements）；
        BATCH 执行器将重用语句并执行批量更新。
        默认SIMPLE
        -->
        <setting name="defaultExecutorType" value="SIMPLE"/>
        <!-- 设置超时时间，它决定驱动等待数据库响应的秒数。 -->
        <setting name="defaultStatementTimeout" value="25"/>

        <setting name="defaultFetchSize" value="100"/>
        <!-- 允许在嵌套语句中使用分页（RowBounds）默认值False -->
        <setting name="safeRowBoundsEnabled" value="false"/>

        <!-- 是否开启自动驼峰命名规则（camel case）映射，
        即从经典数据库列名 A_COLUMN 到经典 Java 属性名 aColumn 的类似映射。
        默认false -->
        <setting name="mapUnderscoreToCamelCase" value="false"/>

        <!--
        MyBatis 利用本地缓存机制（Local Cache）防止循环引用（circular references）和加速重复嵌套查询。
        默认值为 SESSION，这种情况下会缓存一个会话中执行的所有查询。
        若设置值为 STATEMENT，本地会话仅用在语句执行上，对相同 SqlSession 的不同调用将不会共享数据。
        -->
        <setting name="localCacheScope" value="SESSION"/>

        <!--
        当没有为参数提供特定的 JDBC 类型时，为空值指定 JDBC 类型。
        某些驱动需要指定列的 JDBC 类型，多数情况直接用一般类型即可，
        比如 NULL、VARCHAR 或 OTHER。  -->
        <setting name="jdbcTypeForNull" value="OTHER"/>
        
        <!--   指定哪个对象的方法触发一次延迟加载。  -->
        <setting name="lazyLoadTriggerMethods" value="equals,clone,hashCode,toString"/>
    </settings>
</configuration>
```

### 3, 在 application.yml 文件中添加：

```yaml
server:
  port: 8081

spring:
  application:
    name: choss-worker
  profiles:
    active: dev


mybatis:
  config-location: mybatis-config.xml
  mapper-locations:
```

### 4, 代码

luxe.chaos.choss.auth.config.DataSourceConfiguration.java

```java
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

```