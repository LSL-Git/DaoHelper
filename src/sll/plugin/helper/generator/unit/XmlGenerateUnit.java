package sll.plugin.helper.generator.unit;

import sll.plugin.helper.generator.unit.base.FieldUnit;

import java.util.List;

/**
 * xml 生成单元
 * <p>
 * Created by LSL on 2020/1/16 12:11
 */
public class XmlGenerateUnit {

    /**
     * 文件生成目录物理路劲（不含包名）*必填
     */
    private String generatePhysicsPath;

    /**
     * 包名
     */
    private String packageName;

    /**
     * 文件名*必填
     */
    private String xmlName;

    /**
     * 命名空间，对应dao接口
     */
    private String nameSpace;

    /**
     * 返回类型,对应entity
     */
    private String resultType;

    /**
     * 表字段
     */
    private List<FieldUnit> fieldUnits;

    /**
     * 选择生成的方法，insert,delete...
     */
    private List<String> methods;

    /**
     * 对应操作表名
     */
    private String tableName;

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

    public String getXmlName() {
        return xmlName;
    }

    public void setXmlName(String xmlName) {
        this.xmlName = xmlName;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public List<FieldUnit> getFieldUnits() {
        return fieldUnits;
    }

    public void setFieldUnits(List<FieldUnit> fieldUnits) {
        this.fieldUnits = fieldUnits;
    }

    public List<String> getMethods() {
        return methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
