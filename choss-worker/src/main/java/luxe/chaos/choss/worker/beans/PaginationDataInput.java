package luxe.chaos.choss.worker.beans;

import java.io.Serializable;

public class PaginationDataInput implements Serializable {

    private int pageNum = 1;
    private int pageSize = 10;


    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }



}
