package luxe.chaos.choss.worker.entity;

import java.io.Serializable;
import java.util.Date;


public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    // 主键 Primary Key
    private String id;

    // 登录邮箱 Unique Key
    private String account;

    // Not Null
    private String password;

    private String name;

    private Date createTime = new Date();

    private int delSign = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getDelSign() {
        return delSign;
    }

    public void setDelSign(int delSign) {
        this.delSign = delSign;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", createTime='"+ createTime + '\'' +
                '}';
    }
}
