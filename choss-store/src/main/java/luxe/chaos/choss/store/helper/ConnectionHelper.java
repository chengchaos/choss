package luxe.chaos.choss.store.helper;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionHelper implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionHelper.class);


    private static final ConnectionHelper INSTANCE = new ConnectionHelper();

    private static final String ZK_QUORUM = "hbase.zookeeper.quorum";
    private static final AtomicInteger AI = new AtomicInteger(0);


    private Configuration config;
    private  Connection conn;


    public static ConnectionHelper getINSTANCE() {
        return INSTANCE;
    }

    private ConnectionHelper() {
        super();
    }

    public Connection getOrCreateConnection() {

        if (config == null) {
            synchronized (AI) {
                int v = AI.incrementAndGet();
                if (v == 1) {
                    LOGGER.info("AI increment and get v => {}", v);
                    config = HBaseConfiguration.create();
                    config.set(ZK_QUORUM, "192.168.0.111:2181");
                }
            }
        }

        if (conn == null || conn.isClosed()) {
            try {
                this.conn = ConnectionFactory.createConnection(this.config);
            } catch (IOException e) {
                throw new IllegalStateException("Create HBase connection failure !!!", e);
            }
        }
        return conn;
    }



    public Table getTable(String tableName) throws IOException  {
        return this.getOrCreateConnection()
                .getTable(TableName.valueOf(tableName));
    }

    @Override
    public void close() throws Exception {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

    public static void closeConn(ConnectionHelper instance) {
        try {
            instance.close();
        } catch (Exception e) {
            LOGGER.error("HBaseConnection do closing but ... ", e);
        }
    }
}
