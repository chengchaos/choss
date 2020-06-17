package luxe.chaos.choss.sdk;

import luxe.chaos.choss.sdk.impl.ChossClientImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/14 14:25 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
public class ChossClientFactory {

    /**
     * key(String) ==> endpoint_token;
     */
    private static final Map<String, ChossClient> clientCache =
            new ConcurrentHashMap<>();


    /**
     * @param endpoint
     * @param token
     * @return
     */
    public static final ChossClient getOrCreateChossClient(String endpoint, String token) {

        String key = endpoint + "_" + token;
        // 判断是否存在

        if (clientCache.containsKey(key)) {
            return clientCache.get(key);
        }
        // 创建 client 放到 cache 中
        ChossClient client = new ChossClientImpl(endpoint, token);
        clientCache.put(key, client);

//        clientCache.computeIfAbsent(key, () -> new ChøssClientImpl(endpoint, token));


        return client;

    }
}
