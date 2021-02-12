package io.github.jiashunx.app.miniblog.model;

/**
 * @author jiashunx
 */
public class IndexModel {

    private String title;
    private int pageIndex;
    private boolean pageable;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isPageable() {
        return pageable;
    }

    public void setPageable(boolean pageable) {
        this.pageable = pageable;
    }
}
