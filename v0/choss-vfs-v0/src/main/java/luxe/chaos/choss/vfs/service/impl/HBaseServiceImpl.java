package luxe.chaos.choss.vfs.service.impl;

import luxe.chaos.choss.vfs.service.HBaseService;
import org.apache.commons.lang3.StringUtils;
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
import java.util.function.Function;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/13 13:52 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
@Service
public class HBaseServiceImpl implements HBaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HBaseServiceImpl.class);

    private Connection connection;

    private void loggingException(Throwable th) {
        LOGGER.error(StringUtils.EMPTY, th);
    }

    private <R> R withHBaseAdmin(Function<HBaseAdmin, R> theFunction) {
        try (HBaseAdmin admin = (HBaseAdmin) connection.getAdmin()) {
            return theFunction.apply(admin);
        } catch (IOException e) {
            loggingException(e);
            throw new IllegalStateException(e);
        }
    }
    // 1： 创建表

    @Override
    public boolean createTable(String tableName, String[] cfs, byte[][] splitKeys) {

        return this.withHBaseAdmin(admin -> {
            try {
                if (admin.tableExists(tableName)) {
                    return false;
                }
                HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
                Arrays.stream(cfs).forEach(cf -> {
                    HColumnDescriptor columnDescriptor = new HColumnDescriptor(cf);
                    columnDescriptor.setMaxVersions(1);
                    tableDescriptor.addFamily(columnDescriptor);
                });
                admin.createTable(tableDescriptor, splitKeys);
                return true;
            } catch (Exception e) {
                loggingException(e);
            }
            return false;
        });
    }


    // 2： 删除表

    @Override
    public boolean deleteTable(String tableName) {
        try (HBaseAdmin admin = (HBaseAdmin) connection.getAdmin()) {
            if (admin.tableExists(tableName)) {
                admin.disableTable(tableName);
                admin.deleteTable(tableName);
            }
            return true;
        } catch (Exception e) {
            loggingException(e);
        }
        return false;
    }


    // 3： 删除列族

    @Override
    public boolean deleteColumnFamily(String tableName, String columnFamilyName) {
       try (HBaseAdmin admin = (HBaseAdmin) connection.getAdmin()) {
           admin.deleteColumn(tableName, columnFamilyName);
           return true;
       } catch (IOException e) {
           loggingException(e);
       }
       return false;
    }


    // 4： 删除数据



    @Override
    public boolean deleteByInstance(String tableName, Delete delete) {
        try (Table table = connection.getTable(TableName.valueOf(tableName))) {
            table.delete(delete);
            return true;
        } catch (IOException e) {
            LOGGER.error(StringUtils.EMPTY, e);
        }
        return false;
    }

    @Override
    public boolean deleteQualifier(String tableName, String rowKey, String columnFamilyName, String qualifierName) {
        Delete delete = new Delete(Bytes.toBytes(rowKey));
        delete.addColumn(Bytes.toBytes(columnFamilyName), Bytes.toBytes(qualifierName));

        return this.deleteByInstance(tableName, delete);
    }


    // 5： 删除行

    @Override
    public boolean deleteRow(String tableName, String rowKey) {
        Delete delete = new Delete(Bytes.toBytes(rowKey));
        return deleteByInstance(tableName, delete);
    }
    // 6： 读取行

    @Override
    public Result retrieveRow(String tableName, Get get) {
        try (Table table = connection.getTable(TableName.valueOf(tableName))) {
            return table.get(get);
        } catch (IOException e) {
            LOGGER.error(StringUtils.EMPTY, e);
        }
        return null;
    }

    @Override
    public Result retrieveRow(String tableName, String rowKey) {
        Get get = new Get(Bytes.toBytes(rowKey));
        return this.retrieveRow(tableName, get);
    }

    // 7： 获取 scanner

    @Override
    public ResultScanner retrieveScanner(String tableName, Scan scan) {
        try (Table table = connection.getTable(TableName.valueOf(tableName))) {
            return table.getScanner(scan);
        } catch (IOException e) {
            LOGGER.error(StringUtils.EMPTY, e);
        }
        return null;
    }

    @Override
    public ResultScanner retrieveScanner(String tableName, String beginKey, String endKey, FilterList filterList) {

        Scan scan = new Scan();
        scan.setStartRow(Bytes.toBytes(beginKey));
        scan.setStopRow(Bytes.toBytes(endKey));

        scan.setFilter(filterList);
        scan.setCaching(1000);

        return retrieveScanner(tableName, scan);
    }

    // 8： 插入行

    @Override
    public boolean putRow(String tableName, Put put) {
        try (Table table = connection.getTable(TableName.valueOf(tableName))) {
            table.put(put);
            return true;
        } catch (IOException e) {
            LOGGER.error(StringUtils.EMPTY, e);
        }
        return false;
    }


    // 9：批量插入


    @Override
    public boolean putRow(String tableName, List<Put> puts) {
        try (Table table = connection.getTable(TableName.valueOf(tableName))) {
            table.put(puts);
            return true;
        } catch (IOException e) {
            LOGGER.error(StringUtils.EMPTY, e);
        }
        return false;
    }


    @Override
    public long incrementColumnValue(String tableName, String rowKey,
                                     String columnFamilyName, String qualifierName,
                                     long currentValue) {
        try (Table table = connection.getTable(TableName.valueOf(tableName))) {
            return table.incrementColumnValue(Bytes.toBytes(rowKey),
                    Bytes.toBytes(columnFamilyName),
                    Bytes.toBytes(qualifierName),
                    currentValue);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
