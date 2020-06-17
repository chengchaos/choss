package luxe.chaos.choss.rdbs.mybatis;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/11 22:49 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
public class HikariDataSourceFactory extends UnpooledDataSourceFactory {

   public HikariDataSourceFactory()  {
       this.dataSource = new HikariDataSource();
   }
}
