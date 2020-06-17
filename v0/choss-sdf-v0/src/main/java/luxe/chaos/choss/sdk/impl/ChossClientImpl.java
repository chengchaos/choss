package luxe.chaos.choss.sdk.impl;

import com.sun.org.apache.xalan.internal.xsltc.dom.CurrentNodeListFilter;
import luxe.chaos.choss.common.entity.ChossObjectSummary;
import luxe.chaos.choss.common.entity.PutRequest;
import luxe.chaos.choss.common.model.BucketInfo;
import luxe.chaos.choss.common.utils.CoreUtil;
import luxe.chaos.choss.sdk.ChossClient;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/14 14:39 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
public class ChossClientImpl implements ChossClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChossClientImpl.class);

    private String chossServer;
    private String schema;
    private String host;
    private int port = 80;
    private String token;

    private OkHttpClient okHttpClient;


    public ChossClientImpl() {
        super();
    }

    private static class MyInterceptor implements Interceptor {

        @Override
        public Response intercept(okhttp3.Interceptor.Chain chain) throws IOException {
            Request request = chain.request();
            Response response = null;
            boolean success = false;
            int tryCount = 0;
            int maxLimit = 5;
            // 10 秒
            long sleepInterval = 10000L;

            while (!success && tryCount < maxLimit) {
                if (tryCount > 0) {
                    LOGGER.info("intercept: retry request - {}", tryCount);
                }
                response = chain.proceed(request);

                if (response.code() == 404) {
                    break;
                }
                success = response.isSuccessful();

                if (!success) {
                    tryCount++;
                    try {
                        Thread.sleep(sleepInterval);
                    } catch (InterruptedException e) {
                        LOGGER.error("", e);
                        Thread.currentThread().interrupt();
                    }
                }
            }
            return response;
        }
    }

    public ChossClientImpl(String endpoint, String token) {
        super();
        // endpoint ==> http://localhost:8080

        this.chossServer = endpoint;
        String[] ss = endpoint.split("://", 2);
        this.schema = ss[0];
        String[] tt = ss[1].split(":", 2);
        this.host = tt[0];
        this.port = Integer.parseInt(tt[1], 10);

        this.token = token;

        ConnectionPool pool = new ConnectionPool(10, 30, TimeUnit.SECONDS);
        this.okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(120L, TimeUnit.SECONDS)
                .writeTimeout(120L, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .connectionPool(pool)
                .addInterceptor(new MyInterceptor())
                .build();

    }

    private Headers buildHeaders(String token, String contentEncoding, Map<String, String> attrs) {

        final Map<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Auth-Token", token);

        if (Objects.nonNull(contentEncoding)) {
            headerMap.put("content-encoding", contentEncoding);
        }
        if (Objects.nonNull(attrs)) {
            attrs.forEach((key, value) -> headerMap.put("x-choss-arrt_" + key, value));
        }
        return Headers.of(headerMap);

    }

    private Headers buildHeaders(String token) {
        return buildHeaders(token, null, null);
    }

    @Override
    public void createBucket(String bucketName, String bucketDetail) throws IOException {

        Headers headers = this.buildHeaders(token);
        RequestBody requestBody = RequestBody.create(null, CoreUtil.EMPTY_BYTE_ARRAY);

        HttpUrl httpUrl = new HttpUrl.Builder().scheme(schema)
                .host(host)
                .port(port)
                .addPathSegment("/choss/v1/bucket")
                .addQueryParameter("bucket", bucketName)
                .addQueryParameter("detail", bucketDetail)
                .build();

        Request request = new Request.Builder()
                .headers(headers)
                .url(httpUrl)
                .post(requestBody)
                .build();
        Response response = this.okHttpClient.newCall(request).execute();

        if (response.isSuccessful() ) {
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                String msg = responseBody.string();
                response.close();
                throw new RuntimeException(msg);
            }
        }
    }


    @Override
    public void deleteBucket(String bucketName) {

    }

    @Override
    public List<BucketInfo> listBuckets() {
        return null;
    }

    @Override
    public void pubObject(PutRequest putRequest) {

    }

    @Override
    public void pubObject(String bucketName, String key, byte[] content, String mediaType) {

        PutRequest putRequest = new PutRequest();
        putRequest.setBucket(bucketName);
        putRequest.setKey(key);
        putRequest.setFile(null);
        putRequest.setContent(content);
        putRequest.setMediaType(mediaType);

        putRequest.setAttrs(null);
        putRequest.setContentEncoding(null);


        this.pubObject(putRequest);
    }

    @Override
    public void deleteObject(String bucketName, String key) {

    }

    @Override
    public ChossObjectSummary getObjectSummary(String bucketName, String key) {
        return null;
    }
}
