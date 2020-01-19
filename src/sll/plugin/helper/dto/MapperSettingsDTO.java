package sll.plugin.helper.dto;

import java.util.List;

/**
 * Created by LSL on 2020/1/15 14:26
 */
public class MapperSettingsDTO extends BaseSettingsDTO {
    /**
     * 生成的方法
     */
    private List<String> methods;

    public List<String> getMethods() {
        return methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
    }
}
