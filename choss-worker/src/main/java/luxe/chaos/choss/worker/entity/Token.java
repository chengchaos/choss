package luxe.chaos.choss.worker.entity;

import luxe.chaos.choss.util.CodingHelper;
import luxe.chaos.choss.util.DateHelper;

import java.io.Serializable;
import java.util.Date;

public class Token implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private int active = 1;
    private int expireTime = 7;

    private Date refreshTime;
    private Date createTime;


    private String userId;

    public static Token newInstance(String userId) {

        Token token = new Token();
        token.setId(CodingHelper.newUuid());
        token.setUserId(userId);
        Date date = new Date();
        token.setCreateTime(date);
        token.setRefreshTime(date);
        token.setActive(1);
        token.setExpireTime(7);

        return token;
    }

    public static Token newInstanceWithMaxRefreshTime(String userId ) {

        Token token = newInstance(userId);
        Date refreshTime = new Date(DateHelper.YEAR_9999);
        token.setRefreshTime(refreshTime);
        return token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
