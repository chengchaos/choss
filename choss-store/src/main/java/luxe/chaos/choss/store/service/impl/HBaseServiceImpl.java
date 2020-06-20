package luxe.chaos.choss.store.service.impl;

import luxe.chaos.choss.store.helper.ConnectionHelper;
import luxe.chaos.choss.store.service.HBaseService;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class HBaseServiceImpl implements HBaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HBaseServiceImpl.class);

    private final ConnectionHelper connectionHelper = ConnectionHelper.getINSTANCE();


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

    private Optional<ResultScanner> scanner(final String tableName,
                                            final Function<Scan, Scan> function) {
        try (Table table = this.connectionHelper.getTable(tableName)) {
            Scan scan = function.apply(new Scan());
            return Optional.ofNullable(table.getScanner(scan));
        } catch (IOException e) {
            LOGGER.error("Execute HBase scanner, but ...", e);
        }
        return Optional.empty();

    }


    @Override
    public boolean existsTable(String tableName) {
        try (HBaseAdmin admin = (HBaseAdmin) connectionHelper.getOrCreateConnection().getAdmin()) {
            if (admin.tableExists(tableName)) {
                LOGGER.warn("Create HBase table (name={}), but table exists!", tableName);
                return true;
            }
        } catch (IOException e) {
            LOGGER.error("Query HBase table (name = "+ tableName + ") but ...", e);
        }
        return false;
    }

    @Override
    public boolean createTable(String tableName, String[] cfs) {

        try (HBaseAdmin admin = (HBaseAdmin) connectionHelper.getOrCreateConnection().getAdmin()) {

            if (admin.tableExists(tableName)) {
                LOGGER.warn("Create HBase table (name={}), but table exists!", tableName);
                return false;
            }
            final HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
            Arrays.stream(cfs).map(HBaseServiceImpl::newHColumnDescriptor)
                    .forEach(tableDescriptor::addFamily);

            admin.createTable(tableDescriptor);
            return true;
        } catch (IOException e) {
            LOGGER.error("Create HBase table, but ...", e);
        }
        return false;
    }


    @Override
    public boolean createTable(String tableName, String[] columnFamilyArray, byte[][] splitKeys) {

        try (HBaseAdmin admin = (HBaseAdmin) connectionHelper.getOrCreateConnection().getAdmin()) {

            if (admin.tableExists(tableName)) {
                LOGGER.warn("Create HBase table (name={}), but table exists!", tableName);
                return false;
            }

            final HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
            Stream.of(columnFamilyArray)
                    .map(cf -> {
                        HColumnDescriptor desc = new HColumnDescriptor(cf);
                        desc.setMaxVersions(1);
                        return desc;
                    })
                    .forEach(tableDescriptor::addFamily);

            admin.createTable(tableDescriptor, splitKeys);
            return true;
        } catch (Exception e) {
            LOGGER.error("Create HBase table, but ...", e);
        }
        return false;
    }


    @Override
    public boolean deleteTable(String tableName) {
        try (HBaseAdmin admin = (HBaseAdmin) connectionHelper.getOrCreateConnection().getAdmin()) {

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


    @Override
    public boolean deleteColumnFamily(String tableName, String columnFamilyName) {
        try (HBaseAdmin admin = (HBaseAdmin) connectionHelper.getOrCreateConnection().getAdmin()) {

            if (!admin.tableExists(tableName)) {
                return true;
            }
            admin.deleteColumn(tableName, columnFamilyName);
            return true;
        } catch (IOException e) {
            LOGGER.error("Create HBase table, but ...", e);
        }
        return false;

    }


    @Override
    public boolean deleteRow(String tableName, String rowKey) {

        try (Table table = this.connectionHelper.getTable(tableName)) {
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            table.delete(delete);
            return true;
        } catch (IOException e) {
            LOGGER.error("Execute delete HBase row, but ...", e);
        }

        return false;
    }


    @Override
    public boolean deleteQualifier(String tableName, String rowKey, String family, String qualifier) {

        try (Table table = connectionHelper.getTable(tableName)) {

            Delete delete = new Delete(Bytes.toBytes(rowKey));
            delete.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier));
            table.delete(delete);
            return true;
        } catch (IOException e) {
            LOGGER.error("Execute HBase delete qualifier, but ...", e);
        }

        return false;

    }


    @Override
    public boolean putRow(String tableName, String rowKey, String cfName, String qualifier, String data) {

        Put put = newPut(rowKey, cfName, qualifier, data);
        return putRow(tableName, put);
    }


    @Override
    public boolean putRow(String tableName, Put put) {

        try (Table table = connectionHelper.getTable(tableName)) {
            table.put(put);
            return true;
        } catch (IOException e) {
            LOGGER.error("Execute HBase put data, but ...", e);
        }

        return false;
    }


    @Override
    public boolean putRows(String tableName, List<Put> putList) {

        try (Table table = connectionHelper.getTable(tableName)) {
            table.put(putList);
            return true;
        } catch (IOException e) {
            LOGGER.error("Execute HBase put data list, but ...", e);
        }

        return false;
    }


    @Override
    public Optional<Result> getResult(String tableName, String rowKey) {

        try (Table table = connectionHelper.getTable(tableName)) {
            Get get = new Get(Bytes.toBytes(rowKey));
            return Optional.ofNullable(table.get(get));
        } catch (IOException e) {
            LOGGER.error("Execute HBase put data list, but ...", e);
        }

        return Optional.empty();

    }


    @Override
    public Optional<Result> getRow(String tableName, String rowKey,
                                   FilterList filterList) {
        try (Table table = this.connectionHelper.getTable(tableName)) {
            Get get = new Get(Bytes.toBytes(rowKey));
            get.setFilter(filterList);
            return Optional.ofNullable(table.get(get));
        } catch (IOException e) {
            LOGGER.error("Execute HBase getRow but ...", e);
        }

        return Optional.empty();
    }


    /**
     * 全表扫描
     *
     * @param tableName
     * @return
     */
    @Override
    public Optional<ResultScanner> getScanner(final String tableName) {

        return scanner(tableName, scan -> scan.setCaching(1000));

    }

    /**
     * 扫描指定的区间
     *
     * @param tableName
     * @param startRowKey
     * @param stopRowKey
     * @return
     */
    @Override
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
     *
     * @param tableName
     * @param startRowKey
     * @param stopRowKey
     * @param filterList
     * @return
     */
    @Override
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

    @Override
    public boolean existsRow(String tableName, String rowKey) {
        try (Table table = this.connectionHelper.getTable(tableName)) {

            Get get = new Get(Bytes.toBytes(rowKey));
            return table.exists(get);
        } catch (IOException e) {
            LOGGER.error("Execute HBase getRow but ...", e);
        }
        return false;
    }

    @Override
    public long incrementColumnValue(String tableName, String rowKey, String cfName, String qualifier, long value) {

        try (Table table = this.connectionHelper.getTable(tableName)) {

            return table.incrementColumnValue(Bytes.toBytes(rowKey),
                    Bytes.toBytes(cfName),
                    Bytes.toBytes(qualifier),
                    value);

        } catch (IOException e) {
            throw new IllegalStateException("Execute HBase getRow but ...", e);
        }

    }
}
