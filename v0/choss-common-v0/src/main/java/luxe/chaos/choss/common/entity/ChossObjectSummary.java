package luxe.chaos.choss.common.entity;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/13 17:08 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
public class ChossObjectSummary implements Comparable<ChossObjectSummary>, Serializable {

    private String id;
    private String key;
    private String name;
    private String mediaType;
    private String bucket;
    private long length;
    private long lastModifyTime;
    private Map<String, String> attrs;

    @Override
    public int compareTo(ChossObjectSummary o) {
        return this.getKey().compareTo(o.getKey());
    }

    @Override
    public int hashCode() {
        return this.key.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof ChossObjectSummary) {
            ChossObjectSummary that = (ChossObjectSummary) obj;
            return this.key.equals(that.key);
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "ChossObjectSummary{" +
                "id='" + id + '\'' +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", mediaType='" + mediaType + '\'' +
                ", bucket='" + bucket + '\'' +
                ", length=" + length +
                ", lastModifyTime=" + lastModifyTime +
                ", attrs=" + attrs +
                '}';
    }

    public String getContentEncoding() {
        return Objects.isNull(this.attrs) ? null :
                this.attrs.get("content-encoding");
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

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
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
