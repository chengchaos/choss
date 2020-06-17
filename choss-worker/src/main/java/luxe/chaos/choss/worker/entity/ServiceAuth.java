package luxe.chaos.choss.worker.entity;

import java.io.Serializable;
import java.util.Date;

public class ServiceAuth implements Serializable {

    private static final long serialVersionUID = 1L;

    private String bucketId;

    private String tokenId;

    private Date createTime;

    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
