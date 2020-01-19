package sll.plugin.helper.generator.unit.enums;

/**
 * Created by LSL on 2020/1/15 14:27
 */
public enum MapperMethodEnum {
    INSERT("insert", true),
    INSERT_SELECTIVE("insertSelective", true),
    UPDATE("update", true),
    UPDATE_SELECTIVE("updateSelective", true),
    QUERY_ONE("queryOne", true),
    DELETE("delete", true);

    /**
     * 文本
     */
    public String txt;
    /**
     * 默认
     */
    public Boolean def;

    MapperMethodEnum(String txt, Boolean def) {
        this.txt = txt;
        this.def = def;
    }
}
