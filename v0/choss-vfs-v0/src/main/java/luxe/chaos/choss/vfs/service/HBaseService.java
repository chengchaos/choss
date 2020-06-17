package luxe.chaos.choss.vfs.service;

import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.FilterList;

import java.util.List;

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
public interface HBaseService {


    /**
     * 1： 创建表
     * @param tableName Table name
     * @param cfs 列族是组
     * @param splitKeys 预先分区的一些 Keys
     * @return
     */
    boolean createTable( String tableName,
                        String[] cfs,
                        byte[][] splitKeys);

    /**
     * 2: 删除表
     * @return
     */
    boolean deleteTable(String tableName);


    /**
     * 3: 删除列族
     * @param tableName
     * @param columnFamilyName
     * @return
     */
    boolean deleteColumnFamily(String tableName, String columnFamilyName) ;


    /**
     * 4: 删除数据
     */
    boolean deleteByInstance(String tableName, Delete delete);

    boolean deleteQualifier(String tableName, String rowKey, String columnFamilyName, String qualifierName) ;

    /**
     * 5: 删除行数据
     */

    boolean deleteRow(String tableName, String rowKey);


    /**
     * 读取行
     * @param tableName
     * @param get
     */
    Result retrieveRow(String tableName, Get get);

    Result retrieveRow(String tableName, String rowKey);

    ResultScanner retrieveScanner(String tableName, Scan scan);

    ResultScanner retrieveScanner(String tableName, String beginKey, String endKey,
                                  FilterList filterList);

    /**
     * 插入数据
     * @param tableName
     * @param put
     * @return
     */
    boolean putRow(String tableName, Put put);

    /**
     * 批量插入
     * @param tableName
     * @param puts
     * @return
     */
    boolean putRow(String tableName, List<Put> puts);


    // incrementColumnValue

    long incrementColumnValue(String tableName,
                              String rowKey,
                              String columnFamilyName,
                              String qualifierName,
                              long currentValue);
}
