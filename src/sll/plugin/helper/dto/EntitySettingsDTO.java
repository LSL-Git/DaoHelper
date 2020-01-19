package sll.plugin.helper.dto;

import java.util.List;

/**
 * 生成entity配置
 * <p>
 * Created by LSL on 2020/1/14 17:04
 */
public class EntitySettingsDTO extends BaseSettingsDTO {

    /**
     * 是否序列化
     */
    private boolean isSerialize;

    /**
     * 是否添加注解
     */
    private boolean isComment;

    /**
     * 常规方法列表
     */
    private List<String> normalMethods;

    /**
     * lombok 注解
     */
    private List<String> lombokAnnotations;

    public boolean isSerialize() {
        return isSerialize;
    }

    public void setSerialize(boolean serialize) {
        isSerialize = serialize;
    }

    public boolean isComment() {
        return isComment;
    }

    public void setComment(boolean comment) {
        isComment = comment;
    }

    public List<String> getNormalMethods() {
        return normalMethods;
    }

    public void setNormalMethods(List<String> normalMethods) {
        this.normalMethods = normalMethods;
    }

    public List<String> getLombokAnnotations() {
        return lombokAnnotations;
    }

    public void setLombokAnnotations(List<String> lombokAnnotations) {
        this.lombokAnnotations = lombokAnnotations;
    }
}
