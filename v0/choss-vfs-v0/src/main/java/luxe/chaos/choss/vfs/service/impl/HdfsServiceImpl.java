package luxe.chaos.choss.vfs.service.impl;

import luxe.chaos.choss.vfs.config.ChossConfiguration;
import luxe.chaos.choss.vfs.service.HdfsService;
import org.apache.commons.io.FileExistsException;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.function.Consumer;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/13 13:04 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
public class HdfsServiceImpl implements HdfsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HdfsServiceImpl.class);

    private FileSystem fileSystem;

    /**
     * Hadoop 默认的 block size 的大小是 128M
     */
    private final long defaultBlockSize = 128 * 1024 * 1024L;

    /**
     * 如果文件的大小小于 64 M 则手动设置它的 block size 为 64M
     */
    private final long halfDefaultBlockSize = defaultBlockSize >>> 1;


    public HdfsServiceImpl() throws Exception {
        // 1、 读取 hdfs 相关的配置信息
        ChossConfiguration chossConfiguration = ChossConfiguration.getConfiguration();
        // hadoop.conf.dir 其实就是 hadoop/etc/hadoop 目录
        //
        String confDir = chossConfiguration.getString("hadoop.conf.dir");
        // hdfs://hdfs1:9000
        String hdfsUri = chossConfiguration.getString("hadoop.uri");

        // 2、 通过配置信息， 获取一个 fileSystem 对象的实例
        Configuration configuraion = new Configuration();
        configuraion.addResource(new Path(confDir + "/hdfs-size.xml"));
        configuraion.addResource(new Path(confDir + "/core-size.xml"));

        this.fileSystem = FileSystem.get(new URI(hdfsUri), configuraion);
    }

    @Override
    public boolean saveFile(String dir, String name, InputStream inputStream, long length, short replication) throws IOException {
        // 1: 判断 dir 是否存在
        // 1： if dir not exists then create dir
        Path dirPath = new Path(dir);
        try {
            if (!fileSystem.exists(dirPath)) {
                boolean succ = fileSystem.mkdirs(dirPath, FsPermission.getDefault());
                LOGGER.info("create dir ==> {}", dir);
                if (!succ) {
                    throw new RuntimeException("create dir into HDFS failure [dir name = " + dir + "]");
                }
            }
        } catch (FileExistsException e) {
            LOGGER.error(StringUtils.EMPTY, e);
        }
        // 2: save file


        Path path = new Path(dir + Path.SEPARATOR + name);
        long blockSize = halfDefaultBlockSize > length ? defaultBlockSize : halfDefaultBlockSize;
        boolean overwrite = true;
        int bufferSize = 512 * 1024;


        try (InputStream is = inputStream;
             FSDataOutputStream outputStream = fileSystem.create(path, overwrite, bufferSize, replication, blockSize)
        ) {
            fileSystem.setPermission(path, FsPermission.getDefault());
            byte[] buffer = new byte[bufferSize];
            int len = -1;
            while ((len = is.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
        }
        return true;
    }

    @Override
    public boolean deleteFile(String dir, String name) throws IOException {
        return fileSystem.delete(new Path(dir + Path.SEPARATOR +name), false);
    }

    @Override
    public InputStream openFile(String dir, String name) throws IOException {
        return fileSystem.open(new Path(dir + Path.SEPARATOR + name));
    }

    @Override
    public void openFile(String dir, String name, Consumer<InputStream> inputStreamConsumer) {

        try (InputStream is = this.openFile(dir, name)) {
            inputStreamConsumer.accept(is);
        } catch (IOException e) {
            LOGGER.error(StringUtils.EMPTY, e);
        }

    }

    @Override
    public boolean mkdirs(String ... dirNames) throws IOException {
        if (dirNames != null && dirNames.length > 0) {
            String dirName = String.join(Path.SEPARATOR, dirNames);
            return fileSystem.mkdirs(new Path(dirName), FsPermission.getDefault());
        }
        return false;
    }

    @Override
    public boolean deleteDir(String dirName) throws IOException {
        return fileSystem.delete(new Path(dirName), true);
    }

    @Override
    public boolean deleteDir(String[] dirNames) throws IOException {
        if (dirNames != null && dirNames.length > 0) {
            String dirName = String.join(Path.SEPARATOR, dirNames);
            return this.deleteDir(dirName);
        }
        return false;
    }
}
