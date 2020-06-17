package luxe.chaos.choss.store.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ObjectMetaData implements Serializable {

    private static final long serialVersionUID = 1L;

    private String bucketId;

    private String bucketName;

    // 全路径 （RoeKey）
    private String key;

    private String mediaType;

    private long length;
    private long lastModifyTime;

    private Map<String, String> attrs = new HashMap<>();

    public String getContentEncoding() {
        return attrs.get("content-encoding");
    }

    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public Map<String, String> getAttrs() {
        return attrs;
    }

    public void setAttrs(Map<String, String> attrs) {
        this.attrs = attrs;
    }
}
