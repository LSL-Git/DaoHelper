package sll.plugin.helper.generator.mybatis.assembler;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import sll.plugin.helper.dto.EntitySettingsDTO;
import sll.plugin.helper.dto.TableColumnDTO;
import sll.plugin.helper.exception.BaseException;
import sll.plugin.helper.generator.common.JavaReservedWords;
import sll.plugin.helper.generator.unit.ClassGenerateUnit;
import sll.plugin.helper.generator.unit.base.AnnotationUnit;
import sll.plugin.helper.generator.unit.base.FieldUnit;
import sll.plugin.helper.generator.unit.enums.ClassTypeEnum;
import sll.plugin.helper.generator.unit.enums.FieldTypeEnum;
import sll.plugin.helper.generator.unit.enums.LombokAnnotationEnum;
import sll.plugin.helper.generator.unit.enums.NormalMethodEnum;
import sll.plugin.helper.utils.WordConvertUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 实体生成单元组装器
 * <p>
 * Created by LSL on 2020/1/14 15:28
 */
public class EntityGenerateUnitAssembler {

    private EntitySettingsDTO entitySettingsDTO;

    private Map<String, List<TableColumnDTO>> columnsMap;

    public EntityGenerateUnitAssembler(EntitySettingsDTO entitySettingsDTO, Map<String, List<TableColumnDTO>> columnsMap) {
        this.entitySettingsDTO = entitySettingsDTO;
        this.columnsMap = columnsMap;
    }

    /**
     * 执行组装
     *
     * @return list
     */
    public List<ClassGenerateUnit> execute() {
        if (entitySettingsDTO == null || columnsMap == null) {
            throw new BaseException("生成entity信息缺失！");
        }

        boolean isGetter = getNormals(NormalMethodEnum.GETTER.txt);
        boolean isSetter = getNormals(NormalMethodEnum.SETTER.txt);
        boolean isToString = getNormals(NormalMethodEnum.TO_STRING.txt);
        boolean isNoArgsConstructor = getNormals(NormalMethodEnum.NO_ARGS_CONSTRUCTOR.txt);
        boolean isAllArgsConstructor = getNormals(NormalMethodEnum.ALL_ARGS_CONSTRUCTOR.txt);
        boolean isEquals = getNormals(NormalMethodEnum.EQUALS_HASHCODE.txt);

        List<ClassGenerateUnit> classGenerateUnits = new ArrayList<>();
        for (Map.Entry<String, List<TableColumnDTO>> entry : columnsMap.entrySet()) {
            ClassGenerateUnit classGenerateUnit = new ClassGenerateUnit();
            classGenerateUnit.setGeneratePhysicsPath(entitySettingsDTO.getParentPath() + "/" + entitySettingsDTO.getRelativePath());
            classGenerateUnit.setPackageName(entitySettingsDTO.getPackageName());
            classGenerateUnit.setClassType(ClassTypeEnum.CLASS);
            // 根据表名转换类名
            String tableName = entry.getKey();
            String className = WordConvertUtil.getCamelCaseString(tableName, true);
            classGenerateUnit.setClassName(className);
            classGenerateUnit.setTableName(tableName);

            classGenerateUnit.setComment(entitySettingsDTO.isComment());
            classGenerateUnit.setSerialize(entitySettingsDTO.isSerialize());
            classGenerateUnit.setToString(isToString);
            classGenerateUnit.setNoArgsConstructor(isNoArgsConstructor);
            classGenerateUnit.setAllArgsConstructor(isAllArgsConstructor);
            classGenerateUnit.setEquals(isEquals);
            classGenerateUnit.setHashCode(isEquals);

            // 注解
            classGenerateUnit.setClassAnnotations(setAnnotations());

            // 字段
            classGenerateUnit.setFieldUnits(setFields(entry.getValue(), isGetter, isSetter));

            classGenerateUnits.add(classGenerateUnit);
        }

        return classGenerateUnits;
    }

    /**
     * 获取字段
     *
     * @param columnDTOS 列信息
     * @param isGetter   是否生成getter
     * @param isSetter   是否生成setter
     * @return list
     */
    private List<FieldUnit> setFields(List<TableColumnDTO> columnDTOS, boolean isGetter, boolean isSetter) {
        if (columnDTOS == null || columnDTOS.isEmpty()) {
            return null;
        }
        List<FieldUnit> fieldUnits = new ArrayList<>();
        for (TableColumnDTO columnDTO : columnDTOS) {
            FieldUnit fieldUnit = new FieldUnit();
            String fieldName = getFieldName(columnDTO.getColumnName());
            if (StringUtils.isBlank(fieldName)) {
                continue;
            }
            // 获取字段数据库类型
            String dataType = columnDTO.getDataType();
            fieldUnit.setFieldName(fieldName);
            fieldUnit.setColumnName(columnDTO.getColumnName());
            // 获取数据库类型对应的java类型
            FieldTypeEnum fieldTypeEnum = FieldTypeEnum.getEnum(dataType);
            if (fieldTypeEnum ==  null) {
//                throw new BaseException("Java 找不到类型与数据库类型[" + dataType + "]对应！");
                fieldTypeEnum = FieldTypeEnum.JSON;
            }
            fieldUnit.setFieldType(fieldTypeEnum);
            if (fieldTypeEnum.javaType.equals(FieldTypeEnum.DATE.javaType)) {
                fieldUnit.setImportPackage("java.util.Date");
            }
            // 去除注释中的特殊转义符
            fieldUnit.setCommentText(StringEscapeUtils
                    .unescapeJava(columnDTO.getColumnComment())
                    .replaceAll("\n|\r|\t", ""));
            fieldUnit.setGetter(isGetter);
            fieldUnit.setSetter(isSetter);
            fieldUnit.setPrimaryKey(columnDTO.isPrimaryKey());

            fieldUnits.add(fieldUnit);
        }
        return fieldUnits;
    }

    /**
     * 获取字段名称
     * 并校验是否合法
     *
     * @param columnName 列名
     * @return 字段名
     */
    private String getFieldName(String columnName) {
        if (StringUtils.isBlank(columnName)) {
            return null;
        }
        String fieldName = WordConvertUtil.getCamelCaseString(columnName, true);
        fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
        boolean word = JavaReservedWords.containsWord(fieldName);
        if (word) {
            return fieldName + "_";
        } else {
            return fieldName;
        }
    }

    /**
     * 获取注解
     *
     * @return list
     */
    private List<AnnotationUnit> setAnnotations() {
        List<String> lombokAnnotations = entitySettingsDTO.getLombokAnnotations();
        if (lombokAnnotations != null && !lombokAnnotations.isEmpty()) {
            List<AnnotationUnit> annotationUnits = new ArrayList<>();
            for (String annotation : lombokAnnotations) {
                LombokAnnotationEnum annotationEnum = LombokAnnotationEnum.getEnumByTxt(annotation);
                AnnotationUnit annotationUnit = new AnnotationUnit();
                if (annotationEnum != null) {
                    annotationUnit.setAnnotationName(annotationEnum.txt);
                    annotationUnit.setImportPackage(annotationEnum.pack);
                    annotationUnits.add(annotationUnit);
                }
            }
            return annotationUnits;
        }
        return null;
    }

    private boolean getNormals(String methodTxt) {
        List<String> normalMethods = entitySettingsDTO.getNormalMethods();
        if (normalMethods != null && !normalMethods.isEmpty()) {
            for (String normalMethod : normalMethods) {
                if (methodTxt.equals(normalMethod)) {
                    return true;
                }
            }
        }
        return false;
    }

    private EntityGenerateUnitAssembler() {
    }
}
