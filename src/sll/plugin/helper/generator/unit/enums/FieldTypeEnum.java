package sll.plugin.helper.generator.unit.enums;

/**
 * 数据库字段对应java字段枚举
 * <p>
 * Created by LSL on 2020/1/10 17:27
 */
public enum FieldTypeEnum {
    BIGINT("Long", "BIGINT"),
    BINARY("byte[]", "BINARY"),
    BIT("Boolean", "BIT"),
    BLOB("byte[]", "LONGVARBINARY"),
    TINYINT("Byte", "BIT"),
    TINYTEXT("String", "VARCHAR"),
    CHAR("String", "CHAR"),
    DATE("Date", "DATE"),
    DATETIME("Date", "TIMESTAMP"),
    DECIMAL("Long", "DECIMAL"),
    DOUBLE("Double", "DOUBLE"),
    ENUM("String", "CHAR"),
    FLOAT("Float", "REAL"),
    INT("Integer", "INTEGER"),
    LONGBLOB("byte[]", "LONGVARBINARY"),
    LONGTEXT("String", "LONGVARCHAR"),
    MEDIUMBLOB("byte[]", "LONGVARBINARY"),
    MEDIUMINT("Integer", "INTEGER"),
    MEDIUMTEXT("String", "LONGVARCHAR"),
    SET("String", "CHAR"),
    SMALLINT("Short", "SMALLINT"),
    TEXT("String", "LONGVARCHAR"),
    TIME("Date", "TIME"),
    TIMESTAMP("Date", "TIMESTAMP"),
    TINYBLOB("byte[]", "BINARY"),
    VARBINARY("byte[]", "VARBINARY"),
    VARCHAR("String", "VARCHAR"),
    JSON("Object", "OTHER"),
    YEAR("Date", "DATE");


    public String javaType;

    public String jdbcType;

    public static FieldTypeEnum getEnum(String em) {
        for (FieldTypeEnum value : FieldTypeEnum.values()) {
            if (value.toString().toLowerCase().equals(em)) {
                return value;
            }
        }
        return null;
    }

    FieldTypeEnum(String javaType, String jdbcType) {
        this.javaType = javaType;
        this.jdbcType = jdbcType;
    }
}
