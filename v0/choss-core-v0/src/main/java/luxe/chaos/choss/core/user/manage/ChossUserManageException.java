package luxe.chaos.choss.core.user.manage;

import luxe.chaos.choss.core.ChossException;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/12 00:06 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
public class ChossUserManageException extends ChossException {

    private int code;
    private String message;

    public ChossUserManageException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    public ChossUserManageException(int code, String message) {
        super(message, null);
        this.code = code;
        this.message = message;
    }

    @Override
    public int errorCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
