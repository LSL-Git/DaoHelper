package sll.plugin.helper.generator.unit;

import sll.plugin.helper.generator.unit.base.AnnotationUnit;
import sll.plugin.helper.generator.unit.base.FieldUnit;
import sll.plugin.helper.generator.unit.base.MethodUnit;
import sll.plugin.helper.generator.unit.enums.ClassTypeEnum;

import java.util.List;

/**
 * Created by LSL on 2020/1/10 16:37
 */
public class ClassGenerateUnit {

    /**
     * 文件生成目录物理路劲（不含包名）*必填
     */
    private String generatePhysicsPath;

    /**
     * 文件所在包名称
     */
    private String packageName;

    /**
     * 类的类型：class、interface...*必填
     */
    private ClassTypeEnum classType;

    /**
     * 类名称*必填
     */
    private String className;

    /**
     * 改entity或dao接口对应表名称
     */
    private String tableName;

    /**
     * 类相关注解
     */
    private List<AnnotationUnit> classAnnotations;

    /**
     * 字段信息
     */
    private List<FieldUnit> fieldUnits;

    /**
     * 方法信息
     */
    private List<MethodUnit> methodUnits;

    /**
     * 是否序列化
     */
    private boolean isSerialize;

    /**
     * 是否添加注解
     */
    private boolean isComment;

    /**
     * 是否生成toString方法
     */
    private boolean isToString;

    /**
     * 是否生成无参构造方法
     */
    private boolean isNoArgsConstructor;

    /**
     * 是否生成全参构造方法
     */
    private boolean isAllArgsConstructor;

    /**
     * 是否生成equals方法
     */
    private boolean isEquals;

    /**
     * 是否生成hashCode方法
     */
    private boolean isHashCode;

    public String getGeneratePhysicsPath() {
        return generatePhysicsPath;
    }

    public void setGeneratePhysicsPath(String generatePhysicsPath) {
        this.generatePhysicsPath = generatePhysicsPath;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public ClassTypeEnum getClassType() {
        return classType;
    }

    public void setClassType(ClassTypeEnum classType) {
        this.classType = classType;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<AnnotationUnit> getClassAnnotations() {
        return classAnnotations;
    }

    public void setClassAnnotations(List<AnnotationUnit> classAnnotations) {
        this.classAnnotations = classAnnotations;
    }

    public List<FieldUnit> getFieldUnits() {
        return fieldUnits;
    }

    public void setFieldUnits(List<FieldUnit> fieldUnits) {
        this.fieldUnits = fieldUnits;
    }

    public List<MethodUnit> getMethodUnits() {
        return methodUnits;
    }

    public void setMethodUnits(List<MethodUnit> methodUnits) {
        this.methodUnits = methodUnits;
    }

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

    public boolean isToString() {
        return isToString;
    }

    public void setToString(boolean toString) {
        isToString = toString;
    }

    public boolean isNoArgsConstructor() {
        return isNoArgsConstructor;
    }

    public void setNoArgsConstructor(boolean noArgsConstructor) {
        isNoArgsConstructor = noArgsConstructor;
    }

    public boolean isAllArgsConstructor() {
        return isAllArgsConstructor;
    }

    public void setAllArgsConstructor(boolean allArgsConstructor) {
        isAllArgsConstructor = allArgsConstructor;
    }

    public boolean isEquals() {
        return isEquals;
    }

    public void setEquals(boolean equals) {
        isEquals = equals;
    }

    public boolean isHashCode() {
        return isHashCode;
    }

    public void setHashCode(boolean hashCode) {
        isHashCode = hashCode;
    }
}
