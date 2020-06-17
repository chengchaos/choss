package luxe.chaos.choss.common.model;


import luxe.chaos.choss.common.utils.CoreUtil;

import java.util.Date;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/12 18:40 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
public class TokenInfo {

    private String id;

    private String userId;

    private Boolean isActive;

    private int expireTime;

    private Date refreshTime;

    private Date createTime;

    public TokenInfo() {
        super();
    }

    public TokenInfo(String userId) {
        this.userId = userId;
        this.id= CoreUtil.createUuid();
        this.expireTime = 7;
        Date date = new Date();
        this.refreshTime = date;
        this.createTime = date;
        this.isActive = true;
    }

    public static TokenInfo newTokenInfo(String userId) {
        return new TokenInfo(userId);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public int getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }

    public Date getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(Date refreshTime) {
        this.refreshTime = refreshTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "TokenInfo{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", isActive=" + isActive +
                ", expireTime=" + expireTime +
                ", refreshTime=" + refreshTime +
                ", createTime=" + createTime +
                '}';
    }
}
