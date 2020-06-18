package luxe.chaos.choss.store.helper;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
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


    public static Put newPut(String rowKey, String family, String qualifier, String value) {

        return new Put(Bytes.toBytes(rowKey))
                .addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(value));
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
        } catch (IOException e) {
            LOGGER.error("Create HBase table, but ...", e);
        }
        return false;
    }

    public boolean deleteTable(String tableName) {
        try (HBaseAdmin admin = (HBaseAdmin) connHelper.getOrCreateConnection().getAdmin()) {

            if (!admin.tableExists(tableName)) {
                return true;
            }
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
            return true;
        } catch (IOException e) {
            LOGGER.error("Create HBase table, but ...", e);
        }
        return false;
    }

    public boolean deleteColumnFamily(String tableName, String columnName) {
        try (HBaseAdmin admin = (HBaseAdmin) connHelper.getOrCreateConnection().getAdmin()) {

            if (!admin.tableExists(tableName)) {
                return true;
            }
            admin.deleteColumn(tableName, columnName);
            return true;
        } catch (IOException e) {
            LOGGER.error("Create HBase table, but ...", e);
        }
        return false;

    }



    public boolean putRow(String tableName, String rowKey, String cfName, String qualifier, String data) {

        Put put = newPut(rowKey, cfName, qualifier, data);
        return putRow(tableName, put);
    }

    public boolean putRow(String tableName, Put put) {

        try (Table table = connHelper.getTable(tableName)) {
            table.put(put);
            return true;
        } catch (IOException e) {
            LOGGER.error("Execute HBase put data, but ...", e);
        }

        return false;
    }

    public boolean putRows(String tableName, List<Put> putList) {

        try (Table table = connHelper.getTable(tableName)) {
            table.put(putList);
            return true;
        } catch (IOException e) {
            LOGGER.error("Execute HBase put data list, but ...", e);
        }

        return false;
    }

    public Optional<Result> getResult(String tableName, String rowKey) {

        try (Table table = connHelper.getTable(tableName)) {
            Get get = new Get(Bytes.toBytes(rowKey));
            return Optional.ofNullable(table.get(get));
        } catch (IOException e) {
            LOGGER.error("Execute HBase put data list, but ...", e);
        }

        return Optional.empty();

    }

    public Optional<Result> getRow(String tableName, String rowKey,
                                   FilterList filterList) {
        try (Table table = this.connHelper.getTable(tableName)) {
            Get get = new Get(Bytes.toBytes(rowKey));
            get.setFilter(filterList);
            return Optional.ofNullable(table.get(get));
        } catch (IOException e) {
            LOGGER.error("Execute HBase getRow but ...", e);
        }

        return Optional.empty();
    }

    private Optional<ResultScanner> scanner(final String tableName,
                                            final Function<Scan, Scan> function) {
        try (Table table = this.connHelper.getTable(tableName)) {
            Scan scan = function.apply(new Scan());
            return Optional.ofNullable(table.getScanner(scan));
        } catch (IOException e) {
            LOGGER.error("Execute HBase scanner, but ...", e);
        }
        return Optional.empty();

    }
    /**
     * 全表扫描
     * @param tableName
     * @return
     */
    public Optional<ResultScanner> getScanner(final String tableName) {

        return scanner(tableName, scan -> scan.setCaching(1000));

    }

    /**
     * 扫描指定的区间
     * @param tableName
     * @param startRowKey
     * @param stopRowKey
     * @return
     */
    public Optional<ResultScanner> getScanner(final String tableName, final String startRowKey,
                                              final String stopRowKey) {

        return scanner(tableName, scan -> {
            scan.setStartRow(Bytes.toBytes(startRowKey));
            scan.setStopRow(Bytes.toBytes(stopRowKey));
            scan.setCaching(1000);
            return scan;
        });

    }



    /**
     * 使用过滤器
     * @param tableName
     * @param startRowKey
     * @param stopRowKey
     * @param filterList
     * @return
     */
    public Optional<ResultScanner> getScanner(final String tableName, final String startRowKey,
                                              final String stopRowKey, final FilterList filterList) {

        return scanner(tableName, scan -> {
            scan.setStartRow(Bytes.toBytes(startRowKey));
            scan.setStopRow(Bytes.toBytes(stopRowKey));
            scan.setFilter(filterList);
            scan.setCaching(1000);
            return scan;
        });

    }

    public boolean deleteRow(String tableName, String rowKey) {

        try (Table table = this.connHelper.getTable(tableName)) {
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            table.delete(delete);
            return true;
        } catch (IOException e) {
            LOGGER.error("Execute delete HBase row, but ...", e);
        }

        return false;
    }

    public boolean deleteQualifier(String tableName, String rowKey, String family, String qualifier) {

        try (Table table = connHelper.getTable(tableName)) {

            Delete delete = new Delete(Bytes.toBytes(rowKey));
            delete.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier));
            table.delete(delete);
            return true;
        } catch (IOException e) {
            LOGGER.error("Execute HBase delete qualifier, but ...", e);
        }

        return false;

    }

}
