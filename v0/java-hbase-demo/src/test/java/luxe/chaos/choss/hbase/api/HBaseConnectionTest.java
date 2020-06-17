package luxe.chaos.choss.hbase.api;

import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Table;
import org.junit.Test;
import scala.tools.reflect.FormatInterpolator;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/10 21:33 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
public class HBaseConnectionTest {

    @Test
    public void getConnTest() {
        Connection conn = HBaseConnection.getHBaseConn();
        System.out.println("conn is closed ? "+ conn.isClosed());

        HBaseConnection.closeConn();
        System.out.println("conn is closed ? "+ conn.isClosed());

        conn = HBaseConnection.getHBaseConn();
        System.out.println("conn is closed ? "+ conn.isClosed());

        HBaseConnection.closeConn();
        System.out.println("conn is closed ? "+ conn.isClosed());
    }

    @Test
    public void testGetTable() {


        String tableName = "US_POPULATION";

        try {
            Table table = HBaseConnection.getTaable(tableName);
            System.out.println(table.getName().getNameAsString());
            table.close();
            HBaseConnection.closeConn();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
