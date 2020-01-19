package sll.plugin.helper.generator.unit.enums;

/**
 * 常规类方法
 * <p>
 * Created by LSL on 2020/1/14 17:46
 */
public enum NormalMethodEnum {
    GETTER("Getter", true),
    SETTER("Setter", true),
    TO_STRING("toString", true),
    ALL_ARGS_CONSTRUCTOR("全参构造", false),
    NO_ARGS_CONSTRUCTOR("无参构造", false),
    EQUALS_HASHCODE("equals&hashCode", false);

    public String txt;
    public Boolean def;

    NormalMethodEnum(String txt, Boolean def) {
        this.txt = txt;
        this.def = def;
    }
}
