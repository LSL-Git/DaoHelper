package sll.plugin.helper.dto;

/**
 * Created by LSL on 2020/1/15 12:22
 */
public class BaseSettingsDTO {

    /**
     * 工程路径
     */
    private String parentPath;

    /**
     * 相对路径
     */
    private String relativePath;

    /**
     * 包名
     */
    private String packageName;

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
