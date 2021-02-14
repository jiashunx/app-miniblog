package io.github.jiashunx.app.miniblog.model;

import java.util.List;

/**
 * @author jiashunx
 */
public class IndexModel {

    private boolean prevEnabled;
    private List<IndexPage> indexPageList;
    private boolean nextEnabled;

    public boolean isPrevEnabled() {
        return prevEnabled;
    }

    public void setPrevEnabled(boolean prevEnabled) {
        this.prevEnabled = prevEnabled;
    }

    public List<IndexPage> getIndexPageList() {
        return indexPageList;
    }

    public void setIndexPageList(List<IndexPage> indexPageList) {
        this.indexPageList = indexPageList;
    }

    public boolean isNextEnabled() {
        return nextEnabled;
    }

    public void setNextEnabled(boolean nextEnabled) {
        this.nextEnabled = nextEnabled;
    }
}
