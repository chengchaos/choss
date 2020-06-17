package luxe.chaos.choss.vfs.business;

import java.util.List;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/13 17:38 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
public class ObjectListResult {

    private String bucket;
    private String maxKey;
    private String minKey;
    private String nextMarker;

    private int maxKeyNumber;

    private int objectContent;

    private String listId;

    private List<ChossObjectSummary> objectSummaries;

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

    public int getObjectContent() {
        return objectContent;
    }

    public void setObjectContent(int objectContent) {
        this.objectContent = objectContent;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public List<ChossObjectSummary> getObjectSummaries() {
        return objectSummaries;
    }

    public void setObjectSummaries(List<ChossObjectSummary> objectSummaries) {
        this.objectSummaries = objectSummaries;
    }
}
