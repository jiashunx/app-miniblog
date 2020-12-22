package io.github.jiashunx.miniblog.model;

/**
 * @author jiashunx
 */
public class ImageVo implements Cloneable {

    /**
     * 本地存储的文件名.
     */
    private String realName;
    /**
     * 真实名称.
     */
    private String fileName;
    /**
     * 本地存储路径(非绝对路径).
     */
    private String localPath;
    /**
     * 创建时间(毫秒数).
     */
    private long createTimeMillis;
    /**
     * 创建时间(格式化字符串).
     */
    private String createTimeStr;

    @Override
    public ImageVo clone() {
        try {
            return (ImageVo) super.clone();
        } catch (CloneNotSupportedException e) {}
        return null;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public long getCreateTimeMillis() {
        return createTimeMillis;
    }

    public void setCreateTimeMillis(long createTimeMillis) {
        this.createTimeMillis = createTimeMillis;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }
}
