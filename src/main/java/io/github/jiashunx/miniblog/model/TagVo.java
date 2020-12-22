package io.github.jiashunx.miniblog.model;

/**
 * @author jiashunx
 */
public class TagVo implements Cloneable {

    private String tagName;
    private String tagCode;
    private long createTimeMillis;

    @Override
    public TagVo clone() {
        try {
            return (TagVo) super.clone();
        } catch (CloneNotSupportedException e) {}
        return null;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagCode() {
        return tagCode;
    }

    public void setTagCode(String tagCode) {
        this.tagCode = tagCode;
    }

    public long getCreateTimeMillis() {
        return createTimeMillis;
    }

    public void setCreateTimeMillis(long createTimeMillis) {
        this.createTimeMillis = createTimeMillis;
    }
}
