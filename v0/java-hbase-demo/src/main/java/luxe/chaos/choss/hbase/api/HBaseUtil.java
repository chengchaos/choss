package luxe.chaos.choss.hbase.api;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * <strong>
 * HBase 表的创建/查询/删除等操作的工具类
 *
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/10 21:42 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
public class HBaseUtil {

    private static final Logger logger = LoggerFactory.getLogger(HBaseUtil.class);


    private HBaseUtil() {
        super();
        // nothing...
    }

    /**
     * 创建 HBase 表
     * @param tableName 表名称
     * @param cfs 列族的数组
     * @return success ==> true else ==> false;
     */
    public static boolean createTable(String tableName, String[] cfs) {

        try (HBaseAdmin admin = (HBaseAdmin)HBaseConnection.getHBaseConn().getAdmin()) {
            if (admin.tableExists(tableName)) {
                return false;
            }
            HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
            Arrays.stream(cfs).forEach(cf -> {
                HColumnDescriptor columnDescriptor = new HColumnDescriptor(cf);
                columnDescriptor.setMaxVersions(1);
                tableDescriptor.addFamily(columnDescriptor);
            });

            admin.createTable(tableDescriptor);
        } catch (Exception e) {
            logger.error(StringUtils.EMPTY, e);
        }
        return true;
    }

    /**
     *
     * @param tableName 表名
     * @param rowKey RowKey
     * @param cfName 列族名
     * @param qualifier 列标识
     * @param data 数据
     * @return
     */
    public static boolean putRow(String tableName, String rowKey, String cfName, String qualifier, String data) {

        try (Table table = HBaseConnection.getTaable(tableName)) {
            Put put = new Put(Bytes.toBytes(rowKey));
            put.addColumn(Bytes.toBytes(cfName),
                    Bytes.toBytes(qualifier),
                    Bytes.toBytes(data));
            table.put(put);
        } catch (Exception e) {
            logger.error(StringUtils.EMPTY, e);
        }
        return true;
    }

    public static boolean putRows(String tableName, List<Put> putList) {


        try (Table table = HBaseConnection.getTaable(tableName)) {
            table.put(putList);
        } catch (Exception e) {
            logger.error(StringUtils.EMPTY, e);
        }
        return true;
    }

    /**
     *
     * @param tableName
     * @param rowKey
     * @return
     */
    public static Result getRow(String tableName, String rowKey) {

        try (Table table = HBaseConnection.getTaable(tableName)) {
            Get get = new Get(Bytes.toBytes(rowKey));
            return table.get(get);
        } catch (Exception e) {
            logger.error(StringUtils.EMPTY, e);
        }
        return null;

    }

    public static Result getRow(String tableName, String rowKey, FilterList filterList) {
        try (Table table = HBaseConnection.getTaable(tableName)) {
            Get get = new Get(Bytes.toBytes(rowKey));
            get.setFilter(filterList);
            return table.get(get);
        } catch (Exception e) {
            logger.error(StringUtils.EMPTY, e);
        }
        return null;

    }

    public static ResultScanner getScanner(String tableName) {

        try (Table table = HBaseConnection.getTaable(tableName)) {
            Scan scan = new Scan();
            scan.setCaching(1000);

            return table.getScanner(scan);
        } catch (Exception e) {
            logger.error(StringUtils.EMPTY, e);
        }

        return null;
    }

    public static ResultScanner getScanner(String tableName, String startRowKey, String endRowKey) {

        try (Table table = HBaseConnection.getTaable(tableName)) {
            Scan scan = new Scan();
            scan.setStartRow(Bytes.toBytes(startRowKey));
            scan.setStopRow(Bytes.toBytes(endRowKey));

            scan.setCaching(1000);

            return table.getScanner(scan);
        } catch (Exception e) {
            logger.error(StringUtils.EMPTY, e);
        }

        return null;
    }

    public static ResultScanner getScanner(String tableName, String startRowKey, String endRowKey,
                                           FilterList filterList) {

        try (Table table = HBaseConnection.getTaable(tableName)) {
            Scan scan = new Scan();
            scan.setStartRow(Bytes.toBytes(startRowKey));
            scan.setStopRow(Bytes.toBytes(endRowKey));
            scan.setFilter(filterList);
            scan.setCaching(1000);

            return table.getScanner(scan);
        } catch (Exception e) {
            logger.error(StringUtils.EMPTY, e);
        }

        return null;
    }

    public static boolean deleteRow(String tableName, String rowKey) {
        try (Table table = HBaseConnection.getTaable(tableName)) {
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            table.delete(delete);

        } catch (Exception e) {
            logger.error(StringUtils.EMPTY, e);
        }

        return true;
    }

    public static boolean deleteColumnFamily(String tableName, String cfName) {
        try (HBaseAdmin admin = (HBaseAdmin)HBaseConnection.getHBaseConn().getAdmin()) {
            if (admin.tableExists(tableName)) {
                admin.deleteColumn(tableName, cfName);
                return true;
            }
        } catch (Exception e) {
            logger.error(StringUtils.EMPTY, e);
        }
        return false;
    }

    public static boolean deleteQualifier(String tableName,
                                          String rowKey,
                                          String cfName,
                                          String qualifier) {

        try (Table table = HBaseConnection.getTaable(tableName)) {

            Delete delete = new Delete(Bytes.toBytes(rowKey));
            delete.addColumn(Bytes.toBytes(cfName), Bytes.toBytes(qualifier));

            table.delete(delete);

        } catch (Exception e) {
            logger.error(StringUtils.EMPTY, e);
        }

        return true;
    }
}
