package sll.plugin.helper.generator.mybatis.generator;

import org.apache.commons.lang.StringUtils;
import sll.plugin.helper.exception.BaseException;
import sll.plugin.helper.generator.unit.XmlGenerateUnit;
import sll.plugin.helper.generator.unit.base.FieldUnit;
import sll.plugin.helper.generator.unit.enums.MapperMethodEnum;

import java.util.List;

/**
 * XML生成器
 * <p>
 * Created by LSL on 2020/1/13 11:53
 */
public class XmlGenerator extends GeneratorHelper {

    private XmlGenerateUnit xmlGenerateUnit;

    private static final String BASE_MAP_NAME = "BaseMap";
    private static final String BASE_COLUMNS_NAME = "BaseColumns";

    private FieldUnit pKeyField = null;

    public XmlGenerator(XmlGenerateUnit xmlGenerateUnit) {
        this.xmlGenerateUnit = xmlGenerateUnit;
        initPKeyField();
    }

    public void execute() {
        checkExecuteParams();
        // 获取生成文件路径
        String generateFilePath = getGenerateFile();
        // 获取生成文件内容
        String content = getContent();
        // 写出文件
        createFile(generateFilePath, content);
    }

    private String getContent() {
        StringBuilder content = new StringBuilder();
        content
                .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .append(getLine(1))
                .append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">")
                .append(getLine(1))
                .append("<mapper");

        String nameSpace = xmlGenerateUnit.getNameSpace();
        if (StringUtils.isNotBlank(nameSpace)) {
            content
                    .append(" namespace=\"")
                    .append(nameSpace)
                    .append("\">");
        }

        String resultMap = getResultMap();
        if (StringUtils.isNotBlank(resultMap)) {
            content.append(resultMap);
        }

        String baseColumns = getBaseColumns();
        if (StringUtils.isNotBlank(baseColumns)) {
            content.append(baseColumns);
        }

        String xmlTags = getXmlTags();
        if (StringUtils.isNotBlank(xmlTags)) {
            content.append(xmlTags);
        }
        content.append(getLine(1)).append("</mapper>");
        return content.toString();
    }

    /**
     * 拼接查询sql
     *
     * @return sql str
     */
    private String getXmlTags() {
        List<String> methods = xmlGenerateUnit.getMethods();
        List<FieldUnit> fieldUnits = xmlGenerateUnit.getFieldUnits();
        String tableName = xmlGenerateUnit.getTableName();
        String resultType = xmlGenerateUnit.getResultType();
        if (methods != null && !methods.isEmpty()
                && fieldUnits != null && !fieldUnits.isEmpty()
                && StringUtils.isNotBlank(tableName)) {
            StringBuilder sb = new StringBuilder();
            for (String method : methods) {
                // insert
                if (method.equals(MapperMethodEnum.INSERT.txt)
                        && StringUtils.isNotBlank(resultType)) {
                    sb
                            .append(getLineTab())
                            .append("<insert id=\"")
                            .append(method)
                            .append("\" parameterType=\"")
                            .append(resultType)
                            .append("\">")
                            .append(get1Line2Tab())
                            .append("insert into ")
                            .append(tableName)
                            .append(get1Line2Tab())
                            .append("  (");
                    StringBuilder row = new StringBuilder();
                    for (int i = 0; i < fieldUnits.size(); i++) {
                        String columnName = fieldUnits.get(i).getColumnName();
                        row.append(columnName);
                        if (i < fieldUnits.size() - 1) {
                            row.append(", ");
                        }
                        if (row.length() >= 80) {
                            sb
                                    .append(row)
                                    .append(get1Line2Tab())
                                    .append("  ");
                            row = new StringBuilder();
                        }
                    }
                    sb.append(row);

                    sb
                            .append(")")
                            .append(get1Line2Tab())
                            .append("values")
                            .append(get1Line2Tab())
                            .append("  (");
                    row = new StringBuilder();
                    for (int i = 0; i < fieldUnits.size(); i++) {
                        String fieldName = fieldUnits.get(i).getFieldName();
                        row
                                .append("#{")
                                .append(fieldName)
                                .append(",jdbcType=")
                                .append(fieldUnits.get(i).getFieldType().jdbcType)
                                .append("}");
                        if (i < fieldUnits.size() - 1) {
                            row.append(", ");
                        }
                        if (row.length() >= 80) {
                            sb
                                    .append(row)
                                    .append(get1Line2Tab())
                                    .append("  ");
                            row = new StringBuilder();
                        }
                    }
                    sb.append(row);
                    sb
                            .append(")")
                            .append(getLineTab())
                            .append("</insert>");
                }

                // insertSelective
                if (method.equals(MapperMethodEnum.INSERT_SELECTIVE.txt)
                        && StringUtils.isNotBlank(resultType)) {
                    sb
                            .append(getLineTab())
                            .append("<insert id=\"")
                            .append(method)
                            .append("\" parameterType=\"")
                            .append(resultType)
                            .append("\">")
                            .append(get1Line2Tab())
                            .append("insert into ")
                            .append(tableName)
                            .append(get1Line2Tab())
                            .append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
                    for (FieldUnit fieldUnit : fieldUnits) {
                        sb
                                .append(getLine(1))
                                .append(getTab(3))
                                .append("<if test=\"")
                                .append(fieldUnit.getFieldName())
                                .append(" != null\" >")
                                .append(getLine(1))
                                .append(getTab(4))
                                .append(fieldUnit.getColumnName())
                                .append(",")
                                .append(getLine(1))
                                .append(getTab(3))
                                .append("</if>");
                    }
                    sb
                            .append(get1Line2Tab())
                            .append("</trim>")
                            .append(get1Line2Tab())
                            .append("<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >");
                    for (FieldUnit fieldUnit : fieldUnits) {
                        sb
                                .append(getLine(1))
                                .append(getTab(3))
                                .append("<if test=\"")
                                .append(fieldUnit.getFieldName())
                                .append(" != null\" >")
                                .append(getLine(1))
                                .append(getTab(4))
                                .append("#{")
                                .append(fieldUnit.getFieldName())
                                .append(",jdbcType=")
                                .append(fieldUnit.getFieldType().jdbcType)
                                .append("},")
                                .append(getLine(1))
                                .append(getTab(3))
                                .append("</if>");
                    }
                    sb
                            .append(get1Line2Tab())
                            .append("</trim>")
                            .append(getLineTab())
                            .append("</insert>");
                }

                // update
                if (method.equals(MapperMethodEnum.UPDATE.txt)
                        && StringUtils.isNotBlank(resultType)
                        && pKeyField != null) {
                    sb
                            .append(getLineTab())
                            .append("<update id=\"")
                            .append(method)
                            .append("\" parameterType=\"")
                            .append(resultType)
                            .append("\">")
                            .append(get1Line2Tab())
                            .append("update ")
                            .append(tableName)
                            .append(get1Line2Tab())
                            .append("set");
                    int i = 0;
                    for (FieldUnit fieldUnit : fieldUnits) {
                        if (i > 0) {
                            sb.append(",");
                        }
                        sb
                                .append(getLine(1))
                                .append(getTab(3))
                                .append(fieldUnit.getColumnName())
                                .append(" = #{")
                                .append(fieldUnit.getFieldName())
                                .append(",jdbcType=")
                                .append(fieldUnit.getFieldType().jdbcType)
                                .append("}");
                        i++;
                    }
                    sb
                            .append(get1Line2Tab())
                            .append("where ")
                            .append(pKeyField.getColumnName())
                            .append(" = #{")
                            .append(pKeyField.getFieldName())
                            .append(",jdbcType=")
                            .append(pKeyField.getFieldType().jdbcType)
                            .append("}")
                            .append(getLineTab())
                            .append("</update>");
                }

                // updateSelective
                if (method.equals(MapperMethodEnum.UPDATE_SELECTIVE.txt)
                        && StringUtils.isNotBlank(resultType)
                        && pKeyField != null) {
                    sb
                            .append(getLineTab())
                            .append("<update id=\"")
                            .append(method)
                            .append("\" parameterType=\"")
                            .append(resultType)
                            .append("\">")
                            .append(get1Line2Tab())
                            .append("update ")
                            .append(tableName)
                            .append(get1Line2Tab())
                            .append("<set>");
                    for (FieldUnit fieldUnit : fieldUnits) {
                        sb
                                .append(getLine(1))
                                .append(getTab(3))
                                .append("<if test=\"")
                                .append(fieldUnit.getFieldName())
                                .append(" != null\" >")
                                .append(getLine(1))
                                .append(getTab(4))
                                .append(fieldUnit.getColumnName())
                                .append(" = #{")
                                .append(fieldUnit.getFieldName())
                                .append(",jdbcType=")
                                .append(fieldUnit.getFieldType().jdbcType)
                                .append("},")
                                .append(getLine(1))
                                .append(getTab(3))
                                .append("</if>");

                    }
                    sb
                            .append(get1Line2Tab())
                            .append("</set>")
                            .append(get1Line2Tab())
                            .append("where ")
                            .append(pKeyField.getColumnName())
                            .append(" = #{")
                            .append(pKeyField.getFieldName())
                            .append(",jdbcType=")
                            .append(pKeyField.getFieldType().jdbcType)
                            .append("}")
                            .append(getLineTab())
                            .append("</update>");
                }

                // queryOne
                if (method.equals(MapperMethodEnum.QUERY_ONE.txt)
                        && pKeyField != null) {
                    sb
                            .append(getLineTab())
                            .append("<select id=\"")
                            .append(method)
                            .append("\" resultMap=\"")
                            .append(BASE_MAP_NAME)
                            .append("\" parameterType=\"")
                            .append(pKeyField.getFieldType().javaType)
                            .append("\" >")
                            .append(get1Line2Tab())
                            .append("select")
                            .append(get1Line2Tab())
                            .append("<include refid=\"")
                            .append(BASE_COLUMNS_NAME)
                            .append("\" />")
                            .append(get1Line2Tab())
                            .append("from ")
                            .append(tableName)
                            .append(" a")
                            .append(get1Line2Tab())
                            .append("where ")
                            .append("a.")
                            .append(pKeyField.getColumnName())
                            .append(" = #{")
                            .append(pKeyField.getFieldName())
                            .append(",jdbcType=")
                            .append(pKeyField.getFieldType().jdbcType)
                            .append("}")
                            .append(getLineTab())
                            .append("</select>");
                }

                // delete
                if (method.equals(MapperMethodEnum.DELETE.txt)
                        && pKeyField != null) {
                    sb
                            .append(getLineTab())
                            .append("<delete id=\"")
                            .append(method)
                            .append("\" parameterType=\"")
                            .append(pKeyField.getFieldType().javaType)
                            .append("\">")
                            .append(get1Line2Tab())
                            .append("delete from ")
                            .append(tableName)
                            .append(" where ")
                            .append(pKeyField.getColumnName())
                            .append(" = #{")
                            .append(pKeyField.getFieldName())
                            .append(",jdbcType=")
                            .append(pKeyField.getFieldType().jdbcType)
                            .append("}")
                            .append(getLineTab())
                            .append("</delete>");
                }
            }
            return sb.toString();
        }
        return null;
    }

    /**
     * 拼接 base columns
     *
     * @return base columns
     */
    private String getBaseColumns() {
        List<FieldUnit> fieldUnits = xmlGenerateUnit.getFieldUnits();
        if (fieldUnits != null && !fieldUnits.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb
                    .append(getLineTab())
                    .append("<sql id=\"")
                    .append(BASE_COLUMNS_NAME)
                    .append("\">")
                    .append(getLineTab())
                    .append("  ");
            int i = 0;
            StringBuilder row = new StringBuilder();
            for (FieldUnit fieldUnit : fieldUnits) {
                row
                        .append("a.")
                        .append(fieldUnit.getColumnName());
                if (i < fieldUnits.size() - 1) {
                    row.append(", ");
                }
                if (row.length() >= 80) {
                    sb
                            .append(row)
                            .append(getLineTab())
                            .append("  ");
                    row = new StringBuilder();
                }
                i++;
            }
            sb.append(row);
            sb.append(getLineTab())
                    .append("</sql>");
            return sb.toString();
        }
        return null;
    }


    /**
     * 获取resultMap
     *
     * @return result map str
     */
    private String getResultMap() {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(xmlGenerateUnit.getResultType())) {
            sb
                    .append(getLine(1))
                    .append(getTab(1))
                    .append("<resultMap id=\"")
                    .append(BASE_MAP_NAME)
                    .append("\" type=\"")
                    .append(xmlGenerateUnit.getResultType())
                    .append("\">");
            List<FieldUnit> fieldUnits = xmlGenerateUnit.getFieldUnits();
            if (fieldUnits != null && !fieldUnits.isEmpty()) {
                for (FieldUnit fieldUnit : fieldUnits) {
                    sb
                            .append(getLine(1))
                            .append(getTab(2))
                            .append("<")
                            .append(fieldUnit.isPrimaryKey() ? "id" : "result")
                            .append(" column=\"")
                            .append(fieldUnit.getColumnName())
                            .append("\" property=\"")
                            .append(fieldUnit.getFieldName())
                            .append("\" jdbcType=\"")
                            .append(fieldUnit.getFieldType().jdbcType)
                            .append("\" />");
                }
            }
            sb
                    .append(getLine(1))
                    .append(getTab(1))
                    .append("</resultMap>");
            return sb.toString();
        }
        return null;
    }

    /**
     * 获取主键字段
     */
    private void initPKeyField() {
        List<FieldUnit> fieldUnits = xmlGenerateUnit.getFieldUnits();
        if (fieldUnits != null && !fieldUnits.isEmpty()) {
            for (FieldUnit fieldUnit : fieldUnits) {
                if (fieldUnit.isPrimaryKey()) {
                    this.pKeyField = fieldUnit;
                    break;
                }
            }
        }
    }

    /**
     * 获取生成文件
     *
     * @return 文件目录
     */
    private String getGenerateFile() {
        StringBuilder generateFile = new StringBuilder();
        generateFile.append(xmlGenerateUnit.getGeneratePhysicsPath().replaceAll("\\\\", "/"));
        String packageName = xmlGenerateUnit.getPackageName();
        if (StringUtils.isNotBlank(packageName)) {
            generateFile
                    .append("/")
                    .append(packageName.replaceAll("\\.", "/"));
        }
        generateFile
                .append("/")
                .append(xmlGenerateUnit.getXmlName())
                .append(".xml");
        return generateFile.toString();
    }

    /**
     * 校验执行参数
     */
    private void checkExecuteParams() {
        if (xmlGenerateUnit == null) {
            throw new BaseException("生成内容为空！");
        }
        if (StringUtils.isBlank(xmlGenerateUnit.getGeneratePhysicsPath())) {
            throw new BaseException("找不到生成路径！");
        }
        if (xmlGenerateUnit.getXmlName() == null) {
            throw new BaseException("未指定类名！");
        }
    }
}
