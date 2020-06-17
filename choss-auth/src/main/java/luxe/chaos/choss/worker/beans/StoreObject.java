package luxe.chaos.choss.worker.beans;

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
}
