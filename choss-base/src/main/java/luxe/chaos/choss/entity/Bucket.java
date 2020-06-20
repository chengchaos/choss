package luxe.chaos.choss.entity;

import luxe.chaos.choss.util.CodingHelper;

import java.io.Serializable;
import java.util.Date;

public class Bucket implements Serializable {

    private static final long serialVersionUID  = 1L;

    private String id;

    private String name;

    private String userId;

    private String detail;

    private Date createTime;


    public static Bucket newInstance(String userId, String name) {
        Bucket bucket = new Bucket();
        bucket.setId(CodingHelper.newUuid());
        bucket.setCreateTime(new Date());
        bucket.setUserId(userId);
        bucket.setName(name);

        return bucket;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
