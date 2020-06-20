package luxe.chaos.choss.store.helper;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Properties;

public class HDFSTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(HDFSTest.class);


    private Configuration getConfiguration() {
        /*
         * https://www.cnblogs.com/413xiaol/p/9949936.html
         */
        Properties properties = System.getProperties();
        properties.setProperty("HADOOP_USER_NAME", "chengchao");


        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://192.168.0.111:8020");
        return conf;
    }


    /**
     * URL 方式 （了解）
     *
     * @throws Exception 有异常先抛出
     */
    @Test
    public void testUseUrl() throws Exception {

        // 1: 注册 HDFS 的 ulr
        URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());

        // 2: 获取文件输入流
        InputStream inputStream = new URL("hdfs://192.168.0.111:8020/user/3.pdf")
                .openStream();

        // 3: 文件输出流
        FileOutputStream outputStream = new FileOutputStream(new File("c:\\works\\temp\\3.pdf"));

        // 4: Copy file
        IOUtils.copy(inputStream, outputStream);

        IOUtils.closeQuietly(outputStream);
        IOUtils.closeQuietly(inputStream);
    }

    @Test
    public void getFileSystemTest1() {

        Configuration conf = new Configuration();
        // 指定文件系统的类型：
        conf.set("fs.defultFS", "hdfs://192.168.0.111:8020");

        // 2：获取文件系统
        try (FileSystem fileSystem = FileSystem.get(conf)) {
            LOGGER.info("FileSystem => {}", fileSystem);
        } catch (IOException e) {
            LOGGER.error(StringUtils.EMPTY, e);
        }

    }


    @Test
    public void getFileSystemTest2() {

        try (FileSystem fileSystem = FileSystem.get(new URI("hdfs://192.168.0.111:8020"),
                new Configuration())) {
            LOGGER.info("FileSystem => {}", fileSystem);

        } catch (Exception e) {
            LOGGER.error(StringUtils.EMPTY, e);
        }
    }

    @Test
    public void getFileSystemTest3() {

        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://192.168.0.111:8020");
        try (FileSystem fileSystem = FileSystem.newInstance(conf)) {
            LOGGER.info("FileSystem => {}", fileSystem);

        } catch (Exception e) {
            LOGGER.error(StringUtils.EMPTY, e);
        }
    }

    @Test
    public void getFileSystemTest4() {

        try (FileSystem fileSystem = FileSystem.newInstance(
                new URI("hdfs://192.168.0.111:8020"),
                new Configuration()
        )) {
            LOGGER.info("FileSystem => {}", fileSystem);

        } catch (Exception e) {
            LOGGER.error(StringUtils.EMPTY, e);
        }
    }

    /**
     * 遍历 HDFS 中的文件
     */
    @Test
    public void traverseFilesTest() {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://192.168.0.111:8020");
        // 1：获取 FileSystem
        try ( FileSystem fileSystem = FileSystem.newInstance(conf)) {
            // 获得 RemoteIterator
            // 得到所有文件或者文件夹
            // 第一个参数指定遍历的路径
            // 第二个参数指定是否要递归
            RemoteIterator<LocatedFileStatus> iterator = fileSystem.listFiles(new Path("/"), true);
            while (iterator.hasNext()) {
                LocatedFileStatus next = iterator.next();
                LOGGER.info(";) => {}", next.getPath().toString());
            }
        } catch (Exception e) {
            LOGGER.error(StringUtils.EMPTY, e);
        }

    }

    /**
     * 创建文件夹
     */
    @Test
    public void createFolderTest() {
        Configuration conf = getConfiguration();
        // 1：获取 FileSystem
        try ( FileSystem fileSystem = FileSystem.newInstance(conf)) {
            boolean effect = fileSystem.mkdirs(new Path("/user/chengchao/books"));
            LOGGER.info("Make Directory => {}", effect);

        } catch (Exception e) {
            LOGGER.error(StringUtils.EMPTY, e);
            Assertions.fail(e);
        }
    }



    /**
     * 上传文件
     * -Djava.library.path=$HADOOP_HOME/lib/native
     */
    @Test
    public void uploadFileTest() {
        Configuration conf = getConfiguration();
        // 1：获取 FileSystem
        try ( FileSystem fileSystem = FileSystem.newInstance(conf)) {

            long begin = System.currentTimeMillis();
            fileSystem.copyFromLocalFile(new Path("C:\\works\\Downloads\\fma-openjdk_8-jdk-alpine-v1.tar"),
                    new Path("/user/chengchao/fma-openjdk_8-jdk-alpine-v1.tar"));
            long end = System.currentTimeMillis();
            LOGGER.info("Time : => {}", (end - begin) / 1000.0D);
        } catch (Exception e) {
            LOGGER.error(StringUtils.EMPTY, e);
            Assertions.fail(e);
        }
    }

    /**
     * 文件下载
     *
     * -Djava.library.path=$HADOOP_HOME/lib/native
     *
     *
     */
    @Test
    public void downloadFileTest() {
        Configuration conf = getConfiguration();
        // 1：获取 FileSystem
        try (
                FileSystem fileSystem = FileSystem.newInstance(conf);
                FSDataInputStream in = fileSystem.open(new Path("/user/3.pdf"));
                FileOutputStream out = new FileOutputStream(new File("c:\\works\\temp\\4.pdf"))
        ) {

            long begin = System.currentTimeMillis();
            IOUtils.copy(in, out);
            long end = System.currentTimeMillis();
            LOGGER.info("Time : => {}", (end - begin) / 1000.0D);
        } catch (Exception e) {
            LOGGER.error(StringUtils.EMPTY, e);
            Assertions.fail(e);
        }
    }
}
