package luxe.chaos.choss.store.helper;

import luxe.chaos.choss.store.beans.StoreConstants;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.stream.Stream;

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
    public void createTableTest2() {
        String tb = "choss_dir_seq";
        String cf = StoreConstants.CF_NAME_BUCKET_DIR_SEQ;
        boolean result = new HBaseHelper(ConnectionHelper.getINSTANCE())
                .createTable(tb, new String[]{cf}, null);

        LOGGER.info("result => {}", result);
    }

    @Test
    public void deleteTable() {
        String[] tableNames = {
                "chengchao",
                "chengchaos_my-photos",
                "hos_dir_chengchao_photos",
                "hos_obj_chengchao_photos"

        };

        HBaseHelper hBaseHelper = new HBaseHelper(ConnectionHelper.getINSTANCE());

        Stream.of(tableNames)
                .forEach(hBaseHelper::deleteTable);

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
