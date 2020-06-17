package luxe.chaos.choss.hbase.api;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * <p>
 * <strong>
 * HBase 连接类
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/10 21:13 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
public class HBaseConnection {

    private static final Logger logger = LoggerFactory.getLogger(HBaseConnection.class);

    private static final HBaseConnection INSTANCE = new HBaseConnection();
    private Configuration configuration;
    private Connection connection;

    private String zookeeperQuorum = "hdfs1:2181,hdfs2:2181,hdfs3:2181";


    private HBaseConnection(Configuration configuration) {
        this.configuration = configuration;
    }

    private HBaseConnection() {
        try {
            configuration = HBaseConfiguration.create();
            configuration.set("hbase.zookeeper.quorum", zookeeperQuorum);
            HBaseConnection(configuration);

        } catch (Exception e) {
            logger.error(StringUtils.EMPTY, e);
        }
    }

    private Connection getConnection() {
        if (connection == null || connection.isClosed()) {
            try {
                connection = ConnectionFactory.createConnection(configuration);
            } catch (Exception e) {
                logger.error(StringUtils.EMPTY, e);
            }
        }

        return connection;
    }

    /**
     * 获取连接
     * @return
     */
    public static Connection getHBaseConn() {
        return INSTANCE.getConnection();
    }

    /**
     * 获取表 Table
     * @param tableName
     * @return
     * @throws IOException
     */
    public static Table getTaable(String tableName) throws IOException {

        Connection connection = getHBaseConn();
        if (connection != null) {
            return connection.getTable(TableName.valueOf(tableName));
        }
        return null;
    }

    /**
     * 关闭连接
     */
    public static void closeConn() {
        if (INSTANCE.connection != null) {
            try {
                INSTANCE.connection.close();
            } catch (Exception e) {
                logger.error(StringUtils.EMPTY, e);
            }
        }
    }

}
