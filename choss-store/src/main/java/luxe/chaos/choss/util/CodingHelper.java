package luxe.chaos.choss.util;

import io.netty.buffer.ByteBufUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
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
            return ByteBufUtil.hexDump(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 digest failure", e);
        }

    }

    public static void main(String[] args) throws NoSuchAlgorithmException {


        // '2038-01-19 03:14:07' UTC.
        LocalDateTime ldt = LocalDateTime.of(2038, 1, 19,
                3, 14,7);

        Date date = DateHelper.asDate(ldt);
        System.out.println(date);
        System.out.println(date.getTime());
    }
}
