package luxe.chaos.choss.store.beans;

import java.io.Serializable;
import java.util.Map;

public class StoreObjectSummary
        implements Comparable<StoreObjectSummary>, Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String key;
    private String name;
    private long length;
    private String mediaType;
    private long lastModifyTime;
    private String bucket;
    private Map<String, String> attrs;

    @Override
    public int compareTo(StoreObjectSummary o) {
        return this.key.compareTo(o.key);
    }


    public String getContentEncoding() {
        return attrs.get("content-encoding");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public long getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public Map<String, String> getAttrs() {
        return attrs;
    }

    public void setAttrs(Map<String, String> attrs) {
        this.attrs = attrs;
    }

    @Override
    public String toString() {
        return "StoreObjectSummary{" +
                "id='" + id + '\'' +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", length=" + length +
                ", mediaType='" + mediaType + '\'' +
                ", lastModifyTime=" + lastModifyTime +
                ", bucket='" + bucket + '\'' +
                ", attrs=" + attrs +
                '}';
    }
}
