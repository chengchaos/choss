package luxe.chaos.choss.rdbs.mybatis;

import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/11 23:24 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Import(ChossDataSourceConfig.class)
@PropertySource("classpath:application.properties")
@MapperScan("luxe.chaos.choss.**")
@ComponentScan("luxe.chaos.choss.**")
public class BaseTest {
}
