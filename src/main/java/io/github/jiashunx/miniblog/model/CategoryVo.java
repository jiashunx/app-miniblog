package io.github.jiashunx.miniblog.model;

/**
 * @author jiashunx
 */
public class CategoryVo implements Cloneable {

    private String categoryName;
    private String categoryCode;
    private long createTimeMillis;

    @Override
    public CategoryVo clone() {
        try {
            return (CategoryVo) super.clone();
        } catch (CloneNotSupportedException e) {}
        return null;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public long getCreateTimeMillis() {
        return createTimeMillis;
    }

    public void setCreateTimeMillis(long createTimeMillis) {
        this.createTimeMillis = createTimeMillis;
    }
}
