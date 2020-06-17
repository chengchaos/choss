package luxe.chaos.choss.vfs.business;

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
 * @author chengchao - 2020/4/13 17:04 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
public class ObjectMetaData {

    private String backet;
    private String key;
    private String mediaType;
    private long length;
    private long lastModifyTime;
    private Map<String, String> attrs;


    public String getContentEncoding() {
        return Objects.isNull(this.attrs) ? null :
                this.attrs.get("content-encoding");
    }

    public String getBacket() {
        return backet;
    }

    public void setBacket(String backet) {
        this.backet = backet;
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

    @Override
    public String toString() {
        return "ObjectMetaData{" +
                "backet='" + backet + '\'' +
                ", key='" + key + '\'' +
                ", mediaType='" + mediaType + '\'' +
                ", length=" + length +
                ", lastModifyTime=" + lastModifyTime +
                ", attrs=" + attrs +
                '}';
    }
}
