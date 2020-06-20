package luxe.chaos.choss.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class CodingHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(CodingHelper.class);

    public static String newUuid() {
        String uuid = UUID.randomUUID().toString();
        String uuid2 = uuid.replace("-", "");
        LOGGER.info("uuid => {}, 2 => {}, len => {}", uuid, uuid2, uuid2.length());

        return uuid2;
    }

    public static String md5hex(String input) {

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(input.getBytes(StandardCharsets.UTF_8));
            return byteArrayToHexString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 digest failure", e);
        }

    }

    /**
     * 参考：
     * https://www.cnblogs.com/helloz/p/11879377.html
     * @param bytes
     * @return
     */
    public static String byteArrayToHexString(byte[] bytes) {

        if (bytes == null || bytes.length == 0) {
            return StringUtils.EMPTY;
        }

        StringBuilder sb = new StringBuilder(Math.max(bytes.length, 16));

        for (int i = 0, len = bytes.length; i < len - 1; i += 1) {
            String v1 = Integer.toHexString((bytes[i] >> 4) & 0xF);
            String v2 = Integer.toHexString(bytes[i] & 0xF);
            sb.append(v1).append(v2);
        }

        LOGGER.info("array len => {}; String len => {}", bytes.length, sb.length());
        return sb.toString();

    }
    public static void main(String[] args) throws NoSuchAlgorithmException {

        String v1 = md5hex("admin");

        System.out.println(v1);

        // '2038-01-19 03:14:07' UTC.
        LocalDateTime ldt = LocalDateTime.of(2038, 1, 19,
                3, 14,7);

        Date date = DateHelper.asDate(ldt);
        System.out.println(date);
        System.out.println(date.getTime());

        byte[] bytes = new byte[]{23, 23};

        String v2 = byteArrayToHexString(bytes);
        System.out.println(v2);


    }
}
