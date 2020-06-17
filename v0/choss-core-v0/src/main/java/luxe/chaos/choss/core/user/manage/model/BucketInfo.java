package luxe.chaos.choss.core.user.manage.model;

import java.util.Date;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/12 20:01 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
public class BucketInfo {

    /**
     * userId + bucketName
     */
    private String id;

    private String userId;

    private String bucketName;

    private String bucketDetail;

    private Date createTime;


    public BucketInfo() {
        super();
    }

    public static BucketInfo newBucketInfo(String userId, String bucketName) {

        String id = userId + "#" + bucketName;
        BucketInfo bucketInfo = new BucketInfo();
        return bucketInfo.setId(id)
                .setUserId(userId)
                .setBucketName(bucketName)
                .setCreateTime(new Date());

    }

    public String getId() {
        return id;
    }

    public BucketInfo setId(String id) {
        this.id = id;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public BucketInfo setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getBucketName() {
        return bucketName;
    }

    public BucketInfo setBucketName(String bucketName) {
        this.bucketName = bucketName;
        return this;
    }

    public String getBucketDetail() {
        return bucketDetail;
    }

    public BucketInfo setBucketDetail(String bucketDetail) {
        this.bucketDetail = bucketDetail;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public BucketInfo setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    @Override
    public String toString() {
        return "BocketInfo{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", bucketName='" + bucketName + '\'' +
                ", bucketDetail='" + bucketDetail + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
