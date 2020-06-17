package luxe.chaos.choss.common.utils;

import java.util.UUID;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/12 00:17 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
public class CoreUtil {

    private CoreUtil() {
        super();
    }

    public static final String EMPTY_STRING = "";
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];


    /**
     * 中划线 hyphen, dash, minus ...
     * 下划线 underscore, underline ...
     * 正斜杠 forward slash ...
     * 反斜线 back slash ...
     *
     */
    public static final char CHAR_UNDERSCORE = '_';
    public static final char CHAR_HYPHEN = '-';
    public static final char CHAR_FORWARD_SLASH = '/';
    public static final char CHAR_BACK_SLASH = '\\';

    public static final String STR_UNDERSCORE = "_";
    public static final String STR_HYPHEN = "-";
    public static final String STR_FORWARD_SLASH = "/";
    public static final String STR_BACK_SLASH = "\\";


    public static final String SYSTEM_USER = "superadmin";



    public static String createUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");

    }

    public static String createPwd(String pwd) {
        return pwd;
    }
}
