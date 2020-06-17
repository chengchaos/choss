package luxe.chaos.choss.core;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/11 23:32 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
public abstract class ChossException extends RuntimeException {

    protected String errorMessage;

    public ChossException(String message, Throwable cause) {
        super(cause);
        this.errorMessage = message;
    }


    public abstract int errorCode();

    public String getErrorMessage() {
        return errorMessage;
    }
}
