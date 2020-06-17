package luxe.chaos.choss.core;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/12 12:55 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
@SpringBootApplication
//@MapperScan({"luxe.chaos.choss.core.user.manage.dao"})
public class ChossCoreApp {

    public static void main(String[] args) {
        SpringApplication.run(ChossCoreApp.class, args);
    }
}
