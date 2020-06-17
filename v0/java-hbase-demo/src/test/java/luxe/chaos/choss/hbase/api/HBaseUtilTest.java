package luxe.chaos.choss.hbase.api;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import java.util.Collections;

import static org.apache.hadoop.hbase.util.Bytes.toBytes;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/10 22:15 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
public class HBaseUtilTest {

    @Test
    public void createTableTest() {

        boolean createEffect = HBaseUtil.createTable("FileTable",
                new String[]{"fileInfo", "saveInfo"});

        System.out.println("create effect ... "+ createEffect);


    }

    @Test
    public void addFileInfo() {
        String tableName = "FileTable";
        String rowKey = "rowkey0004";

        HBaseUtil.putRow(tableName, rowKey,
                "fileInfo",
                "name",
                "在家办公.jpg"
        );
        HBaseUtil.putRow(tableName, rowKey,
                "fileInfo",
                "type",
                "jpg"
        );
        HBaseUtil.putRow(tableName, rowKey,
                "fileInfo",
                "size",
                "10086"
        );
        HBaseUtil.putRow(tableName, rowKey,
                "saveInfo",
                "creator",
                "Charles"
        );

        System.out.println("add data effect -=> ");

    }

    @Test
    public void getFileDetails() {
        String tableName = "FileTable";
        String rowKey = "rowkey0001";
        Result result = HBaseUtil.getRow(tableName, rowKey);

        if (result != null) {

            System.out.println("rowkey = "+ Bytes.toString(result.getRow()));
            System.out.println("fileName = "+ Bytes.toString(
                    result.getValue(toBytes("fileInfo"), toBytes("name"))
            ));
            System.out.println("fileSize = "+ Bytes.toString(
                    result.getValue(toBytes("fileInfo"), toBytes("size"))
            ));
        } else {
            System.out.println("result is null :( .");
        }
    }

    @Test
    public void scanFileDetailsTest() {

        String tableName = "FileTable";

        try (ResultScanner resultScanner = HBaseUtil.getScanner(tableName, "rowkey0001", "rowkey0004")) {
            if (resultScanner != null) {
                resultScanner.forEach(result -> {
                    System.out.println(" --- ");
                    System.out.println("rowkey = "+ Bytes.toString(result.getRow()));
                    System.out.println("fileName = "+ Bytes.toString(
                            result.getValue(toBytes("fileInfo"), toBytes("name"))
                    ));
                    System.out.println("fileSize = "+ Bytes.toString(
                            result.getValue(toBytes("fileInfo"), toBytes("size"))
                    ));
                });
            }
        }


//        resultScanner.close();
    }

    @Test
    public void deleteRowTest() {
        String tableName = "FileTable";
        HBaseUtil.deleteRow(tableName, "rowkey0001");
    }

    @Test
    public void deleteTableTest() {

    }


    @Test
    public void rowFilterTest() {

        String tableName = "FileTable";

        Filter filter = new RowFilter(CompareFilter.CompareOp.EQUAL,
                new BinaryComparator(Bytes.toBytes("rowkey0003")));
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL, Collections.singletonList(filter));
        ResultScanner resultScanner = HBaseUtil.getScanner(tableName, "rowkey0001", "rowkey0004", filterList);

        if (resultScanner != null) {

            try {
                resultScanner.forEach(result -> {
                    System.out.println(" --- ");
                    System.out.println("rowkey = "+ Bytes.toString(result.getRow()));
                    System.out.println("fileName = "+ Bytes.toString(
                            result.getValue(toBytes("fileInfo"), toBytes("name"))
                    ));
                    System.out.println("fileSize = "+ Bytes.toString(
                            result.getValue(toBytes("fileInfo"), toBytes("size"))
                    ));
                });
            } finally {
                resultScanner.close();
            }
        }

    }


    @Test
    public void prefixFilterTest() {

        String tableName = "FileTable";

        Filter filter = new PrefixFilter(toBytes("rowkey0003"));

        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL, Collections.singletonList(filter));
        ResultScanner resultScanner = HBaseUtil.getScanner(tableName, "rowkey0001", "rowkey0004", filterList);

        if (resultScanner != null) {

            try {
                resultScanner.forEach(result -> {
                    System.out.println(" --- ");
                    System.out.println("rowkey = "+ Bytes.toString(result.getRow()));
                    System.out.println("fileName = "+ Bytes.toString(
                            result.getValue(toBytes("fileInfo"), toBytes("name"))
                    ));
                    System.out.println("fileSize = "+ Bytes.toString(
                            result.getValue(toBytes("fileInfo"), toBytes("size"))
                    ));
                });
            } finally {
                resultScanner.close();
            }
        }

    }


    @Test
    public void keyOnlyFilterTest() {

        String tableName = "FileTable";

        Filter filter = new KeyOnlyFilter(true);

        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL, Collections.singletonList(filter));
        ResultScanner resultScanner = HBaseUtil.getScanner(tableName, "rowkey0001", "rowkey0004", filterList);

        if (resultScanner != null) {

            try {
                resultScanner.forEach(result -> {
                    System.out.println(" --- ");
                    System.out.println("rowkey = "+ Bytes.toString(result.getRow()));
                    System.out.println("fileName = "+ Bytes.toString(
                            result.getValue(toBytes("fileInfo"), toBytes("name"))
                    ));
                    System.out.println("fileSize = "+ Bytes.toString(
                            result.getValue(toBytes("fileInfo"), toBytes("size"))
                    ));
                });
            } finally {
                resultScanner.close();
            }
        }

    }



    @Test
    public void columnPrefixFilterTest() {

        String tableName = "FileTable";

        Filter filter = new ColumnPrefixFilter(toBytes("nam"));

        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL, Collections.singletonList(filter));
        ResultScanner resultScanner = HBaseUtil.getScanner(tableName, "rowkey0001", "rowkey0004", filterList);

        if (resultScanner != null) {

            try {
                resultScanner.forEach(result -> {
                    System.out.println(" --- ");
                    System.out.println("rowkey = "+ Bytes.toString(result.getRow()));
                    System.out.println("fileName = "+ Bytes.toString(
                            result.getValue(toBytes("fileInfo"), toBytes("name"))
                    ));
                    System.out.println("fileSize = "+ Bytes.toString(
                            result.getValue(toBytes("fileInfo"), toBytes("size"))
                    ));
                });
            } finally {
                resultScanner.close();
            }
        }

    }
}

