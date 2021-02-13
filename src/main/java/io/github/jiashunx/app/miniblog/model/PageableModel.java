package io.github.jiashunx.app.miniblog.model;

import java.util.List;

/**
 * @author jiashunx
 */
public class PageableModel {

    private boolean prevEnabled;
    private List<PageIndex> pageIndexList;
    private boolean nextEnabled;

    public boolean isPrevEnabled() {
        return prevEnabled;
    }

    public void setPrevEnabled(boolean prevEnabled) {
        this.prevEnabled = prevEnabled;
    }

    public List<PageIndex> getPageIndexList() {
        return pageIndexList;
    }

    public void setPageIndexList(List<PageIndex> pageIndexList) {
        this.pageIndexList = pageIndexList;
    }

    public boolean isNextEnabled() {
        return nextEnabled;
    }

    public void setNextEnabled(boolean nextEnabled) {
        this.nextEnabled = nextEnabled;
    }
}
