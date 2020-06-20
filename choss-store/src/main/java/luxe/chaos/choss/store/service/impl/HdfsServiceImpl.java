package luxe.chaos.choss.store.service.impl;

import com.jcraft.jsch.IO;
import luxe.chaos.choss.store.config.ChossConfiguration;
import luxe.chaos.choss.store.service.HdfsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.security.auth.login.AppConfigurationEntry;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class HdfsServiceImpl implements HdfsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HdfsServiceImpl.class);

    @Autowired
    private ChossConfiguration chossConfiguration;

    private FileSystem fileSystem;

    /**
     *
     */
    private static long defaultBlockSize = 1024 * 1024 * 128;

    private static long initBlockSize = defaultBlockSize / 2;

    public HdfsServiceImpl() {
        super();
    }

    @PostConstruct
    public void execPostConstruct() {
        // 1: 读取 hdfs 信息
//        String confDir = chossConfiguration.getString("hadoop.config.dir");
        String hdfsUri = chossConfiguration.getString("hadoop.hdfs.uri");

        // 2： 创建 FileSystem 对象
        Configuration conf = new Configuration();
//        conf.addResource(new Path(confDir + "/hdfs-core.xml"));
//        conf.addResource(new Path(confDir + "/hdfs-site.xml"));

        try {
            fileSystem = FileSystem.get(new URI(hdfsUri), conf);
        } catch (Exception e) {
            throw new IllegalArgumentException("Get HDFS File System ... but", e);
        }

    }

    @PreDestroy
    public void execPreDestroy() {

        if (this.fileSystem != null) {
            try {
                this.fileSystem.close();
            } catch (IOException e) {
                LOGGER.error(StringUtils.EMPTY, e);
            }
        }
    }


    @Override
    public void saveFile(String dir, String fileName, InputStream in, long length, short replication) {
        // 1. 目录是否存在？ 不存在则创建、
        Path dirPath = new Path(dir);

        try {
            if (!fileSystem.exists(dirPath)) {
                boolean createDirSuccess = fileSystem.mkdirs(dirPath, FsPermission.getDirDefault());
                LOGGER.info("Create dir => {}", dirPath);
                if (!createDirSuccess) {
                    throw new RuntimeException("Kao !!!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 2. 保持文件
        Path path = new Path(dir + "/" + fileName);

        long blockSize = length <= initBlockSize ? initBlockSize : defaultBlockSize;

        boolean overwrite = true;
        int bufferSize = 1024 * 512;


        try (FSDataOutputStream out = fileSystem.create(path, overwrite,
                bufferSize, replication, blockSize)) {
            fileSystem.setPermission(path, FsPermission.getFileDefault());
            byte[] buffer = new byte[bufferSize];
            int len = -1;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {
            LOGGER.error(StringUtils.EMPTY, e);
        }
    }

    @Override
    public void deleteFile(String dir, String fileName) {

        try {
            fileSystem.delete(new Path(dir), false);
        } catch (IOException e) {
            LOGGER.error(StringUtils.EMPTY, e);
        }
    }

    @Override
    public InputStream openFile(String dir, String fileName) throws IOException {

        return fileSystem.open(new Path(dir + "/" + fileName));
    }

    @Override
    public void mkDirs(String dir) {
        try {
            fileSystem.mkdirs(new Path(dir), FsPermission.getDirDefault());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rmDirs(String dir) {
        try {
            fileSystem.delete(new Path(dir), true);
        } catch (IOException e) {
            LOGGER.error(StringUtils.EMPTY, e);
        }
    }
}
