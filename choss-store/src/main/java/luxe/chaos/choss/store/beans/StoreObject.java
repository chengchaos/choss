package luxe.chaos.choss.store.beans;

import okhttp3.Response;

import java.io.InputStream;

public class StoreObject implements AutoCloseable {

    private ObjectMetaData objectMetaData;
    private InputStream inputStream;
    // okhttp
    private Response response;

    public StoreObject() {
        super();
    }

    public StoreObject(Response response) {
        this.response = response;
    }

    @Override
    public void close() throws Exception {

        if (this.inputStream != null) {
            inputStream.close();
        }

        if (response != null) {
            this.response.close();
        }
    }

    public ObjectMetaData getObjectMetaData() {
        return objectMetaData;
    }

    public void setObjectMetaData(ObjectMetaData objectMetaData) {
        this.objectMetaData = objectMetaData;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
