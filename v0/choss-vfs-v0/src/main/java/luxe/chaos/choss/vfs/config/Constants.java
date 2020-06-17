package luxe.chaos.choss.vfs.config;

import org.apache.hadoop.hbase.util.Bytes;

import java.nio.charset.StandardCharsets;

/**
 * <p>
 * <strong>
 * 常量类
 * </strong><br /><br />
 * 保存： 表名称， 列族名称， 列名称等名字。
 *
 *
 * </p>
 *
 * @author chengchao - 2020/4/13 11:10 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
public class Constants {

    private Constants() {
        super();
    }

    /**
     * 在 HBase 中创建的目录表和文件表的前缀
     * chøss_dir_{bucket_name}
     */
    public static final String TABLE_PREFIX_DIR = "choss_dir_";
    public static final String TABLE_PREFIX_OBJ = "choss_obj_";

    /**
     * 目录表的列族名称 1：
     * 目录表的元数据
     */
    public static final String CF_DIR_META = "cf";
    static final byte[] CF_DIR_META_BYTES = CF_DIR_META.getBytes(StandardCharsets.UTF_8);

    public static byte[] getCfDirMetaBytes() {
       return CF_DIR_META_BYTES;
    }

    /**
     * 目录表的列族名称  2：
     * 子目录
     */
    public static final String CF_DIR_SUBS = "sub";

    static final byte[] CF_DIR_SUBS_BYTES = CF_DIR_SUBS.getBytes(StandardCharsets.UTF_8);


    public static byte[] getCfDirSubsBytes() {
        return CF_DIR_SUBS_BYTES;
    }

    /**
     * 对象表的列族名称 1：
     * 元数据
     */
    public static final String CF_OBJ_META = "cf";

    static final byte[] CF_OBJ_META_BYTES = CF_OBJ_META.getBytes(StandardCharsets.UTF_8);

    public static byte[] getCfObjMetaBytes() {
        return CF_OBJ_META_BYTES;
    }
    /**
     * 对象表的列作名称 2：
     * 文件内容
     */
    public static final String CF_OBJ_CONT = "cont";

    static final byte[] CF_OBJ_CONT_BYTES = CF_OBJ_CONT.getBytes(StandardCharsets.UTF_8);

    public static byte[] getCfObjContBytes() {
        return CF_OBJ_CONT_BYTES;
    }

    public static final String QUALIFIER_DIR_SEQ_ID = "u";

    public static final String QUALIFIER_OBJ_CONT = "c";
    public static final String QUALIFIER_OBJ_LEN = "l";
    public static final String QUALIFIER_OBJ_PROPS = "p";
    public static final String QUALIFIER_OBJ_MEDIA_TYPE = "m";

    /**
     * 在 HDBS 上 Choss 的根目录
     */
    public static final String FILE_STORE_ROOT = "/choss";

    /**
     * 数据文件大小的上限，超过这个值保存到 hdfs 中
     * 20 m
     */
    public static final int FILE_STORE_THRESHOLD = 20 * 1024 * 1024;


    /**
     * 存储 HBase 目录表 SeqID 的表
     */
    public static final String TABLE_BUCKET_DIR_SEQ = "choss_dir_seq";

    /**
     * CF 值
     */
    public static final String CF_BUCKET_DIR_SEQ = "s";

    static final byte[] CF_BUCKET_DIR_SEQ_BYTES = CF_BUCKET_DIR_SEQ.getBytes(StandardCharsets.UTF_8);

    public static byte[] getCfBucketDirSeqBytes() {
        return CF_BUCKET_DIR_SEQ_BYTES;
    }

    /**
     * 列名称值
     */
    public static final String QUALIFIER_BUCKET_DIR_SEQ = "s";
    private static final byte[] QUALIFIER_BUCKET_DIR_SEQ_BYTES = Bytes.toBytes(QUALIFIER_BUCKET_DIR_SEQ);

    public static byte[] getQualifierBucketDirSeqBytes() {
        return QUALIFIER_BUCKET_DIR_SEQ_BYTES;
    }

    public static String getTableNameOfDir(String bucketName) {
        return TABLE_PREFIX_DIR + bucketName;
    }

    public static String getTableNameOfObj(String bucketName) {
        return TABLE_PREFIX_OBJ + bucketName;
    }

    private static final String[] columnFamilyOfDir = {CF_DIR_META, CF_DIR_SUBS};

    private static final String[] columnFamilyOfObj = {CF_OBJ_META, CF_OBJ_CONT};

    /**
     * 获取目录表的所有的列族
     */
    public static String[] getColumnFamilysOfDir() {
        return columnFamilyOfDir;
    }

    public static String[] getColumnFamilyOfObj() {
        return columnFamilyOfObj;
    }

    private static byte[][] OBJ_REGIONS = new byte[][]{
            Bytes.toBytes("1"),
            Bytes.toBytes("4"),
            Bytes.toBytes("7"),
    };

    public static byte[][] getObjReginos() {
        return OBJ_REGIONS;
    }
}
