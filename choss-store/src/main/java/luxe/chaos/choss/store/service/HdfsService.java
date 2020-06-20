package luxe.chaos.choss.store.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public interface HdfsService {


    void saveFile(String dir, String fileName, InputStream is, long length,
                  short replication);

    void deleteFile(String dir, String fileName);

    InputStream openFile(String dir, String name) throws IOException;

    void mkDirs(String dir);

    void rmDirs(String dir);
}
