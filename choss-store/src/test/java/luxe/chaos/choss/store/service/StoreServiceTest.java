package luxe.chaos.choss.store.service;

import luxe.chaos.choss.store.ChossStoreApplication;
import luxe.chaos.choss.store.beans.StoreObject;
import luxe.chaos.choss.store.beans.StoreObjectSummary;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(classes = ChossStoreApplication.class,
webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class StoreServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(StoreServiceTest.class);

    @Autowired
    private StoreService storeService;

    @Test
    public void createBucketStoreTest() {

        String bucketId = "chengchao_photos";
        try {
            this.storeService.createBucketStore(bucketId);
        } catch (IOException e) {
            LOGGER.error(StringUtils.EMPTY, e);
            Assertions.fail(e);
        }
    }

    @Test
    public void deleteBucketStoreTest() {
        String bucketId = "chengchao_photos";
        try {
            this.storeService.deleteBucketStore(bucketId);
        } catch (IOException e) {
            LOGGER.error(StringUtils.EMPTY, e);
            Assertions.fail(e);
        }
    }

    @Test
    public void putObjectTest() {
        String bucketId = "chengchao_photos";

        File file = new File("C:\\works\\git-repo\\github.com\\chengchaos\\choss\\.gitignore");
        String rowKey = "/pdffile/downfile/pc/gitignore.txt";
//        rowKey = "/images/internet/photos/";

//        file = new File("C:\\works\\Downloads\\jdk-11.0.1_windows-x64_bin.zip");
//        rowKey = "/pdffile/downfile/pc/jdk-11.0.1_windows-x64_bin.zip";

        long length = file.length();
        ByteBuffer content = ByteBuffer.allocate((int)length);

        try (FileChannel fc = FileChannel.open(file.toPath())) {
            fc.read(content);
            LOGGER.info("content => {}", content);
        } catch (IOException e) {
            e.printStackTrace();
        }


        String mediaType = "application/pdf";
        Map<String, String> props = new HashMap<>();
        props.put("desc", "test");

        try {
            this.storeService.putObject(bucketId, rowKey,
                    content, length, mediaType, props
                    );
        } catch (IOException e) {
            LOGGER.error(StringUtils.EMPTY, e);
            Assertions.fail(e);
        }
    }


    @Test
    public void getSummaryDirTest() {
        String bucketId = "chengchao_photos";
        String rowKey = "/images/internet/photos/";

        try {
            StoreObjectSummary summary = this.storeService.getSummary(bucketId, rowKey);

            LOGGER.info("summary => {}", summary);

        } catch (IOException e) {
            LOGGER.error(StringUtils.EMPTY, e);
            Assertions.fail(e);
        }
    }


    @Test
    public void getSummaryObjTest() {
        String bucketId = "chengchao_photos";
        String rowKey = "/pdffile/downfile/pc/gitignore.txt";

        try {
            StoreObjectSummary summary = this.storeService.getSummary(bucketId, rowKey);
            LOGGER.info("summary => {}", summary);

        } catch (IOException e) {
            LOGGER.error(StringUtils.EMPTY, e);
            Assertions.fail(e);
        }
    }



    @Test
    public void getObjectTest1() {
        String bucketId = "chengchao_photos";
        String rowKey = "/pdffile/downfile/pc/gitignore.txt";

        StoreObject storeObject = null;
        try {
            storeObject = this.storeService.getObject(bucketId, rowKey);

            LOGGER.info("storeObject => {}", storeObject);

            try (InputStream inputStream = storeObject.getInputStream();
                 InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                 BufferedReader reader =new BufferedReader(inputStreamReader) ) {

                String line = null;

                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

        } catch (IOException e) {
            LOGGER.error(StringUtils.EMPTY, e);
            Assertions.fail(e);
        } finally {
            if (storeObject != null) {
                try {
                    storeObject.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }




    @Test
    public void getObjectTest2() {
        String bucketId = "chengchao_photos";

        File file = new File("C:\\works\\chaos-temp\\jdk-11.0.1_windows-x64_bin.zip");
        String rowKey = "/pdffile/downfile/pc/jdk-11.0.1_windows-x64_bin.zip";

        try {
            StoreObject storeObject = this.storeService.getObject(bucketId, rowKey);

            LOGGER.info("storeObject => {}", storeObject);

            try (InputStream inputStream = storeObject.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(file)) {

                byte[] buff = new byte[8192];
                int len = 0;
                while ((len = inputStream.read(buff)) > 0) {
                    outputStream.write(buff, 0, len);
                }
            }

        } catch (IOException e) {
            LOGGER.error(StringUtils.EMPTY, e);
            Assertions.fail(e);
        }
    }

    @Test
    public void deleteObjectTest() {

        String bucketId = "chengchao_photos";

        File file = new File("C:\\works\\chaos-temp\\jdk-11.0.1_windows-x64_bin.zip");
        String fileName = "/pdffile/downfile/pc/jdk-11.0.1_windows-x64_bin.zip";
        fileName = "/pdffile/downfile/pc/";


        try {

            this.storeService.deleteObject(bucketId, fileName);

        } catch (IOException e) {
            LOGGER.error(StringUtils.EMPTY, e);
            Assertions.fail(e);
        }

    }
}
