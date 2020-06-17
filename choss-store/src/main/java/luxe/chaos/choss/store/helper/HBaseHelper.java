package luxe.chaos.choss.store.helper;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class HBaseHelper {


    private static final Logger LOGGER = LoggerFactory.getLogger(HBaseHelper.class);

    private ConnectionHelper connHelper;

    public HBaseHelper() {
        super();
    }

    public HBaseHelper(ConnectionHelper connHelper) {
        this.connHelper = connHelper;
    }

    public void setConnHelper(ConnectionHelper connHelper) {
        this.connHelper = connHelper;
    }

    private static HColumnDescriptor newHColumnDescriptor(String cf) {
        HColumnDescriptor columnDescriptor = new HColumnDescriptor(cf);
        columnDescriptor.setMaxVersions(1);
        return columnDescriptor;
    }


    public static Put newPut(String rowKey, String cfName, String qualifier, String data) {

        return new Put(Bytes.toBytes(rowKey))
                .addColumn(Bytes.toBytes(cfName), Bytes.toBytes(qualifier), Bytes.toBytes(data));
    }

    public static List<Put> newPut(final String rowKey, final String cfName,
                                   Map<String, String> datas) {

        return datas.entrySet()
                .stream()
                .map(entry -> newPut(rowKey, cfName, entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

    }


    public boolean createTable(String tableName, String[] cfs) {

        try (HBaseAdmin admin = (HBaseAdmin) connHelper.getOrCreateConnection().getAdmin()) {

            if (admin.tableExists(tableName)) {
                return false;
            }
            HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
            Arrays.stream(cfs).map(HBaseHelper::newHColumnDescriptor)
                    .forEach(tableDescriptor::addFamily);

            admin.createTable(tableDescriptor);
            return true;
        } catch (IOException ioex) {
            LOGGER.error("Create HBase table, but ...", ioex);
        }
        return false;
    }


    public boolean putRow(String tableName,
                          String rowKey,
                          String cfName,
                          String qualifier,
                          String data) {

        Put put = newPut(rowKey, cfName, qualifier, data);
        return putRow(tableName, put);
    }

    public boolean putRow(String tableName, Put put) {

        try (Table table = connHelper.getTable(tableName)) {
            table.put(put);
            return true;
        } catch (IOException ioex) {
            LOGGER.error("Execute HBase put data, but ...", ioex);
        }

        return false;
    }

    public boolean putRows(String tableName, List<Put> putList) {

        try (Table table = connHelper.getTable(tableName)) {
            table.put(putList);
            return true;
        } catch (IOException ioex) {
            LOGGER.error("Execute HBase put data list, but ...", ioex);
        }

        return false;
    }

    public Optional<Result> getResult(String tableName, String rowKey) {

        try (Table table = connHelper.getTable(tableName)) {
            Get get = new Get(Bytes.toBytes(rowKey));
            return Optional.ofNullable(table.get(get));
        } catch (IOException ioex) {
            LOGGER.error("Execute HBase put data list, but ...", ioex);
        }

        return Optional.empty();

    }
}
