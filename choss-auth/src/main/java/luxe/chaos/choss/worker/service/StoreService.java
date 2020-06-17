package luxe.chaos.choss.worker.service;

import luxe.chaos.choss.worker.beans.ObjectListResult;
import luxe.chaos.choss.worker.beans.StoreObjectSummary;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

public interface StoreService {

    void createBucketStore() throws IOException;

    void deleteBucketStore() throws IOException;

    void createSeqTable() throws IOException;

    void pub(String bucket, String key,
             ByteBuffer content, long length,
             String mediaType,
             Map<String, String> props) throws IOException;

    StoreObjectSummary getSummary(String bucket, String key) throws IOException;

    List<StoreObjectSummary> listSummaries(String bucket, String startKey, String endKey) throws IOException;

    ObjectListResult listDir(String bucket, String dir, String start, int maxCount) throws IOException;

    ObjectListResult listByPrefix(String bucket, String dir, String start, String prefix, int maxCount) throws  IOException;

    void deleteObject(String bucket, String key) throws IOException;




}
