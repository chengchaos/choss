package luxe.chaos.choss.store.helper;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Table;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ConnectionHelperTest {


    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionHelperTest.class);

//    private HBaseConnection helper = HBaseConnection.getINSTANCE();


    private void connAndClose() {
        Connection conn = null;
        try {
            TimeUnit.MILLISECONDS.sleep(10L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try (ConnectionHelper helper = ConnectionHelper.getINSTANCE()) {
            conn = helper.getOrCreateConnection();
            LOGGER.info("conn 1 => {}", conn.isClosed());
            Assertions.assertNotNull( conn, "conn not null!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.info("conn 2 => {}", conn.isClosed());
    }

    @Test
    public void connCloseTest() {

        for (int i  = 0; i < 10; i++ ) {
            new Thread(this::connAndClose).start();
        }

        try {
            TimeUnit.SECONDS.sleep(5L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getTableTest() {

        String tableName= "chengchao";
        try (ConnectionHelper hc = ConnectionHelper.getINSTANCE()){
            Table table = hc.getTable(tableName);
            LOGGER.info("table => {}", table);
        } catch (Exception e) {
            LOGGER.error(StringUtils.EMPTY, e);
        }
    }
}
