package luxe.chaos.choss.worker.beans;

import java.util.List;

public class ObjectListResult {

    private String bucket;
    private String maxKey;
    private String minKey;
    private String nextMarker;
    private int maxKeyNumber;
    private int objectCount;
    private String listId;
    private List<StoreObjectSummary> objectSummaries;

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getMaxKey() {
        return maxKey;
    }

    public void setMaxKey(String maxKey) {
        this.maxKey = maxKey;
    }

    public String getMinKey() {
        return minKey;
    }

    public void setMinKey(String minKey) {
        this.minKey = minKey;
    }

    public String getNextMarker() {
        return nextMarker;
    }

    public void setNextMarker(String nextMarker) {
        this.nextMarker = nextMarker;
    }

    public int getMaxKeyNumber() {
        return maxKeyNumber;
    }

    public void setMaxKeyNumber(int maxKeyNumber) {
        this.maxKeyNumber = maxKeyNumber;
    }

    public int getObjectCount() {
        return objectCount;
    }

    public void setObjectCount(int objectCount) {
        this.objectCount = objectCount;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public List<StoreObjectSummary> getObjectSummaries() {
        return objectSummaries;
    }

    public void setObjectSummaries(List<StoreObjectSummary> objectSummaries) {
        this.objectSummaries = objectSummaries;
    }
}
