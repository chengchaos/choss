package luxe.chaos.choss.store.service;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.filter.FilterList;

import java.util.List;
import java.util.Optional;

public interface HBaseService {


    boolean existsTable(String table);

    boolean createTable(String tableName, String[] cfs);

    boolean createTable(String tableName, String[] columnFamilyArray, byte[][] splitKeys);

    boolean deleteTable(String tableName);

    boolean deleteColumnFamily(String tableName, String columnFamilyName);


    boolean deleteRow(String tableName, String rowKey);


    /**
     *
     * @param tableName Table Name
     * @param rowKey RowKey
     *
     * @param family column family
     * @param qualifier qualifier
     * @return
     */
    boolean deleteQualifier(String tableName, String rowKey, String family, String qualifier);


    boolean putRow(String tableName, String rowKey, String cfName, String qualifier, String data);

    boolean putRow(String tableName, Put put);

    boolean putRows(String tableName, List<Put> putList);

    Optional<Result> getResult(String tableName, String rowKey);

    Optional<Result> getRow(String tableName, String rowKey,
                            FilterList filterList);


    /**
     * 全表扫描
     *
     * @param tableName
     * @return
     */
    Optional<ResultScanner> getScanner(final String tableName);

    /**
     * 扫描指定的区间
     *
     * @param tableName
     * @param startRowKey
     * @param stopRowKey
     * @return
     */
    Optional<ResultScanner> getScanner(final String tableName, final String startRowKey,
                                       final String stopRowKey);


    /**
     * 使用过滤器
     *
     * @param tableName
     * @param startRowKey
     * @param stopRowKey
     * @param filterList
     * @return
     */
    Optional<ResultScanner> getScanner(final String tableName, final String startRowKey,
                                       final String stopRowKey, final FilterList filterList);

    boolean existsRow(String tableName, String rowKey);

    long incrementColumnValue(String tableName, String rowKey, String cfName, String qualifier, long value);


}
