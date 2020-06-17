package luxe.chaos.choss.store.helper;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class HBaseHelperTest {


    private static final Logger LOGGER = LoggerFactory.getLogger(HBaseHelperTest.class);
    String tableName = "chengchao";
    String cfName = "cf";

    @Test
    public void createTableTest() {
        String cf = "cf";
        boolean result = new HBaseHelper(ConnectionHelper.getINSTANCE())
                .createTable(tableName, new String[]{cf});

        LOGGER.info("result => {}", result);
    }

    @Test
    public void addDataTest() {

        boolean result = new HBaseHelper(ConnectionHelper.getINSTANCE())
                .putRow(tableName, "wk00001", cfName,
                        "name", "程超");

        LOGGER.info("result => {}", result);
    }


    @Test
    public void getDataTest() {


        Optional<Result> resultOptional = new HBaseHelper(ConnectionHelper.getINSTANCE())
                .getResult(tableName, "wk00001");

        if (resultOptional.isPresent()) {
            Result result = resultOptional.get();

            LOGGER.info("result => {}", result);
            byte[] bytes = result.getValue(Bytes.toBytes(cfName),
                    Bytes.toBytes("name" ));
            LOGGER.info("name => {}", new String(bytes));
        }
    }
}
