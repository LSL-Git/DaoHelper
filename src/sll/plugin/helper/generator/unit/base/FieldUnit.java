package sll.plugin.helper.generator.unit.base;

import sll.plugin.helper.generator.unit.enums.FieldTypeEnum;

/**
 * 字段基本单位
 * <p>
 * Created by LSL on 2020/1/10 16:50
 */
public class FieldUnit {
    /**
     * 字段类型 Integer\String\Long ...
     */
    private FieldTypeEnum fieldType;

    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 列名
     */
    private String columnName;

    /**
     * 字段对应需要导入的包名
     */
    private String importPackage;

    /**
     * 注解文本
     */
    private String commentText;

    /**
     * 是否是静态字段
     */
    private boolean isStatic;

    /**
     * 是否是final字段
     */
    private boolean isFinal;

    /**
     * 生成getter
     */
    private boolean isGetter;

    /**
     * 生成setter
     */
    private boolean isSetter;

    /**
     * 是否主键
     */
    private boolean isPrimaryKey;

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    public FieldTypeEnum getFieldType() {
        return fieldType;
    }

    public void setFieldType(FieldTypeEnum fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getImportPackage() {
        return importPackage;
    }

    public void setImportPackage(String importPackage) {
        this.importPackage = importPackage;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public boolean isGetter() {
        return isGetter;
    }

    public void setGetter(boolean getter) {
        isGetter = getter;
    }

    public boolean isSetter() {
        return isSetter;
    }

    public void setSetter(boolean setter) {
        isSetter = setter;
    }
}
