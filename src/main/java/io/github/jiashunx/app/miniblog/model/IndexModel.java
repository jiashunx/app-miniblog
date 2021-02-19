package io.github.jiashunx.app.miniblog.model;

import java.util.List;

/**
 * @author jiashunx
 */
public class IndexModel {

    private List<IndexRow> indexRowList;
    private PageableIndex prevPageableIndex;
    private List<PageableIndex> pageableIndexList;
    private PageableIndex nextPageableIndex;

    public List<IndexRow> getIndexRowList() {
        return indexRowList;
    }

    public void setIndexRowList(List<IndexRow> indexRowList) {
        this.indexRowList = indexRowList;
    }

    public List<PageableIndex> getPageableIndexList() {
        return pageableIndexList;
    }

    public void setPageableIndexList(List<PageableIndex> pageableIndexList) {
        this.pageableIndexList = pageableIndexList;
    }

    public PageableIndex getPrevPageableIndex() {
        return prevPageableIndex;
    }

    public void setPrevPageableIndex(PageableIndex prevPageableIndex) {
        this.prevPageableIndex = prevPageableIndex;
    }

    public PageableIndex getNextPageableIndex() {
        return nextPageableIndex;
    }

    public void setNextPageableIndex(PageableIndex nextPageableIndex) {
        this.nextPageableIndex = nextPageableIndex;
    }
}
