package luxe.chaos.choss.store.beans;

import org.apache.hadoop.hbase.util.Bytes;

public class StoreConstants {

    /**
     * 目录表表名称前缀
     */
    public static final String TABLE_PREFIX_DIR = "choss_dir_";

    /**
     * 对象表表名称前缀
     */
    public static final String TABLE_PREFIX_OBJ = "choss_obj_";

    /**
     * 生成序列号的表名称
     */
    public static final String TABLE_NAME_BUCKET_DIR_SEQ = "choss_dir_seq";

    /**
     * 生成序列号的列族名称
     */
    public static final String CF_NAME_BUCKET_DIR_SEQ = "s";

    /**
     * 目录表 meta 列族名称
     */
    public static final String CF_NAME_DIR_META = "cf";

    /**
     * 目录表 subdir 列族名称
     */
    public static final String CF_NAME_DIR_SUBS = "sub";


    /**
     * 文件表 META 列族名称
     */
    public static final String CF_NAME_OBJ_META = "cf";

    /**
     * 文件表 content 列族名称
     */
    public static final String CF_NAME_OBJ_CONT = "c";


    /**
     * 目录表 SEQ_ID qualifier
     */
    public static final String QUALIFIER_BUCKET_SEQ_ID = "u";

    public static final String QUALIFIER_OBJ_CONTENT = "c";
    public static final String QUALIFIER_OBJ_LEN = "l";
    public static final String QUALIFIER_OBJ_PROPS = "p";
    public static final String QUALIFIER_OBJ_MEDIA_TYPE = "m";
    public static final String QUALIFIER_DIR_SEQ_ID = "u";

    /**
     * 在 HDFS 中存储文件的跟目录
     */
    public static final String FILE_STORE_ROOT = "/choss";

    /**
     * 在 HBase 存储的文件 size 的上限。
     *  20 M (2 << 23) + (2 << 21)
     */
    public static final int FILE_STORE_THRESHOLD = 1024 * 1024 * 20;

    /**
     * 预先分区的东西
     */
    public static final byte[][] OBJ_REGIONS = new byte[][] {
            Bytes.toBytes("1"),
            Bytes.toBytes("4"),
            Bytes.toBytes("7")
    };


    public static final String getDirTableName(String bucketId) {

        return TABLE_PREFIX_DIR + bucketId;
    }

    public static final String getObjTableName(String bucketId) {

        return TABLE_PREFIX_OBJ + bucketId;
    }

    public static final String[] getColumnFamilyForDir() {

        return new String[]{CF_NAME_DIR_META, CF_NAME_DIR_SUBS};
    }

    public static final String[] getColumnFamilyForoObj() {

        return new String[]{CF_NAME_OBJ_META, CF_NAME_OBJ_CONT};
    }
}
