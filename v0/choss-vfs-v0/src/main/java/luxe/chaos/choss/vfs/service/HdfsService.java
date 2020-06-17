package luxe.chaos.choss.vfs.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/13 12:50 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
public interface HdfsService {

    /**
     *
     * @param dir 目录
     * @param name 名称
     * @param inputStream InputStream
     * @param length 文件长度
     * @param replication 备份数量
     */
    boolean saveFile(String dir, String name, InputStream inputStream, long length, short replication) throws IOException;

    boolean deleteFile(String dir, String name) throws IOException ;

    InputStream openFile(String dir, String name) throws IOException;

    void openFile(String dir, String name, Consumer<InputStream> inputStreamConsumer);

    boolean mkdirs(String ... dirName) throws IOException;

    boolean deleteDir(String dirName) throws IOException;

    boolean deleteDir(String[] dirName) throws IOException;
}
