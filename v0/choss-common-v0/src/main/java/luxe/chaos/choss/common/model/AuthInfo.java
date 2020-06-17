package luxe.chaos.choss.common.model;

import java.util.Date;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/12 18:45 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
public class AuthInfo {

    private String bucketId;
    private String targetToken;
    private Date createTime;

    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }

    public String getTargetToken() {
        return targetToken;
    }

    public void setTargetToken(String targetToken) {
        this.targetToken = targetToken;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ServiceAuth{" +
                "bucketId='" + bucketId + '\'' +
                ", targetToken='" + targetToken + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
