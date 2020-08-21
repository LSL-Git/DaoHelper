package sll.plugin.helper.generator.unit.enums;

/**
 * Created by LSL on 2020/1/14 17:54
 */
public enum LombokAnnotationEnum {
    DATA("@Data", true, "lombok.Data"),
    ALL_ARGS_CONSTRUCTOR("@AllArgsConstructor", false, "lombok.AllArgsConstructor"),
    NO_ARGS_CONSTRUCTOR("@NoArgsConstructor", false, "lombok.NoArgsConstructor"),
    TO_STRING("@ToString", false, "lombok.ToString"),
    ACCESSORS("@Accessors(chain = true)", false, "lombok.experimental.Accessors"),
    GETTER("@Getter", false, "lombok.Getter"),
    SETTER("@Setter", false, "lombok.Setter");

    /**
     * 显示文本
     */
    public String txt;
    /**
     * 默认值
     */
    public Boolean def;
    /**
     * 对于包路径
     */
    public String pack;

    LombokAnnotationEnum(String txt, Boolean def, String pack) {
        this.txt = txt;
        this.def = def;
        this.pack = pack;
    }

    public static LombokAnnotationEnum getEnumByTxt(String txt) {
        for (LombokAnnotationEnum value : LombokAnnotationEnum.values()) {
            if (txt.equals(value.txt)) {
                return value;
            }
        }
        return null;
    }
}
