package io.github.jiashunx.app.miniblog.model;

/**
 * @author jiashunx
 */
public class PageableIndex {
    /**
     * 分页是否忽略(...)
     */
    private boolean ignore;
    /**
     * 分页数字
     */
    private int indexNo;
    /**
     * url.
     */
    private String indexUrl;
    /**
     * 是否当前展示页
     */
    private boolean current;

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    public int getIndexNo() {
        return indexNo;
    }

    public void setIndexNo(int indexNo) {
        this.indexNo = indexNo;
    }

    public String getIndexUrl() {
        return indexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public boolean getCurrent() {
        return isCurrent();
    }

    public boolean getIgnore() {
        return isIgnore();
    }
}
