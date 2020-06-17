package luxe.chaos.choss.store.beans;

import java.io.Serializable;
import java.util.List;

public class  PaginationDataOutput<T> implements Serializable {

    private long currentPage;
    private long maxPage;
    private long pageSize;
    private long totality;
    private List<T> data;

    public long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
    }

    public long getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(long maxPage) {
        this.maxPage = maxPage;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotality() {
        return totality;
    }

    public void setTotality(long totality) {
        this.totality = totality;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PaginationDataOutput{" +
                "currentPage=" + currentPage +
                ", maxPage=" + maxPage +
                ", pageSize=" + pageSize +
                ", totality=" + totality +
                ", data=" + data +
                '}';
    }
}
