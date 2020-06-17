package luxe.chaos.choss.core.utils;

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

    public static final String SYSTEM_USER = "superadmin";

    public static String createUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");

    }

    public static String createPwd(String pwd) {
        return pwd;
    }
}
