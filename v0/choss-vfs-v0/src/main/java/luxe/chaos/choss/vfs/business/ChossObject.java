package luxe.chaos.choss.vfs.business;

import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.InputStream;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * <p>
 * <strong>
 * 用一句话描述功能
 * </strong><br /><br />
 * 如题
 * </p>
 *
 * @author chengchao - 2020/4/13 17:18 <br />
 * @see [相关类方法]
 * @since [产品模块版本]
 */
public class ChossObject implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChossObject.class);

    private ObjectMetaData metaData;

    private InputStream content;

    private Response response;


    public ChossObject() {
        super();
    }

    public ChossObject(Response response) {
        this.response = response;
    }

    @Override
    public void close() throws Exception {
        this.closeAll(content, response);
    }

    private void closeAll(Closeable... closeables) {
        if (closeables != null && closeables.length > 0) {
            Stream.of(closeables)
                    .filter(Objects::nonNull)
                    .forEach(this::doClose);
        }
    }

    private void doClose(Closeable c) {
        try {
            c.close();
        } catch (Exception e) {
            LOGGER.error(StringUtils.EMPTY, e);
        }
    }


    public ObjectMetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(ObjectMetaData metaData) {
        this.metaData = metaData;
    }

    public InputStream getContent() {
        return content;
    }

    public void setContent(InputStream content) {
        this.content = content;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
