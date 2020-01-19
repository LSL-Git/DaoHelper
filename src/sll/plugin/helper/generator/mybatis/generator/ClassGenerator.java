package sll.plugin.helper.generator.mybatis.generator;

import org.apache.commons.lang.StringUtils;
import sll.plugin.helper.exception.BaseException;
import sll.plugin.helper.generator.unit.ClassGenerateUnit;
import sll.plugin.helper.generator.unit.base.AnnotationUnit;
import sll.plugin.helper.generator.unit.base.FieldUnit;
import sll.plugin.helper.generator.unit.base.MethodUnit;
import sll.plugin.helper.generator.unit.enums.FieldTypeEnum;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 类文件生成器
 * <p>
 * Created by LSL on 2020/1/13 11:52
 */
public class ClassGenerator extends GeneratorHelper {

    /**
     * 类生成单元
     */
    private ClassGenerateUnit classGenerateUnit;

    private Set<String> classImportSet = new LinkedHashSet<>();
    private Set<String> fieldImportSet = new LinkedHashSet<>();
    private Set<String> annotationImportSet = new LinkedHashSet<>();

    public ClassGenerator(ClassGenerateUnit classGenerateUnit) {
        this.classGenerateUnit = classGenerateUnit;
    }

    /**
     * 执行生成文件
     */
    public void execute() {
        // 校验输入参数
        checkExecuteParams();
        String generateFile = getGenerateFile();
        // 创建文件并写入内容
        createFile(generateFile, getContent());
    }

    /**
     * 拼接生成类内容
     *
     * @return content
     */
    private String getContent() {
        StringBuilder content = new StringBuilder();
        String packageDTO = getPackage();
        String annotationDTO = getAnnotations();
        String clazzDTO = getClazz();
        String fieldDTO = getFields();
        String methodsDTO = getMethods();
        String importsDTO = getImports();

        if (StringUtils.isNotBlank(packageDTO)) {
            content.append(packageDTO).append(getLine(2));
        }

        if (StringUtils.isNotBlank(importsDTO)) {
            content.append(importsDTO).append(getLine(1));
        }

        if (StringUtils.isNotBlank(annotationDTO)) {
            content.append(annotationDTO);
        }

        content.append(clazzDTO).append(getLine(1));

        if (StringUtils.isNotBlank(fieldDTO)) {
            content.append(fieldDTO);
        }

        if (StringUtils.isNotBlank(methodsDTO)) {
            content.append(methodsDTO);
        }
        content
                .append(getLine(1))
                .append("}");
        return content.toString();
    }

    /**
     * 获取注解拼接结果
     *
     * @return 字符串
     */
    private String getAnnotations() {
        List<AnnotationUnit> classAnnotations = classGenerateUnit.getClassAnnotations();
        if (classAnnotations == null || classAnnotations.isEmpty()) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (AnnotationUnit classAnnotation : classAnnotations) {
            result.append(classAnnotation.getAnnotationName()).append(getLine(1));
            annotationImportSet.add(classAnnotation.getImportPackage());
        }
        return result.toString();
    }

    /**
     * 解析拼接类方法 字符串
     *
     * @return 字符串
     */
    private String getMethods() {
        StringBuilder result = new StringBuilder();
        List<FieldUnit> fieldUnits = classGenerateUnit.getFieldUnits();
        if (fieldUnits != null) {
            // 是否生成toString方法
            boolean toString = classGenerateUnit.isToString();
            // toString 方法拼接结果
            StringBuilder toStringStr = initToStringStr();

            // 是否生成 equals方法
            boolean equals = classGenerateUnit.isEquals();
            // equals方法拼接结果
            StringBuilder equalsStr = initEqualsStr();

            // 是否生成hashCode方法
            boolean hashCode = classGenerateUnit.isHashCode();
            // hashCode方法拼接结果
            StringBuilder hashCodeStr = initHashCodeStr();

            // 是否生成全参构造方法
            boolean allArgsConstructor = classGenerateUnit.isAllArgsConstructor();
            StringBuilder allArgsConstructorStr = initAllArgsConstructor();
            StringBuilder allArgs = new StringBuilder();

            // 是否生成无参构造方法
            boolean noArgsConstructor = classGenerateUnit.isNoArgsConstructor();
            int i = 0;
            for (FieldUnit fieldUnit : fieldUnits) {
                if (fieldUnit.isGetter()) {
                    result
                            .append(getLine(1))
                            .append("\r\tpublic ")
                            .append(fieldUnit.getFieldType().javaType)
                            .append(" ").append(getGetterMethodName(fieldUnit.getFieldName()))
                            .append("() {")
                            .append("\r\t\treturn ")
                            .append(fieldUnit.getFieldName())
                            .append(";")
                            .append(getLine(1))
                            .append("\t}");
                }
                if (fieldUnit.isSetter()) {
                    result
                            .append(getLine(1))
                            .append("\r\tpublic void ")
                            .append(getSetterMethodName(fieldUnit.getFieldName()))
                            .append("(").append(fieldUnit.getFieldType().javaType)
                            .append(" ").append(fieldUnit.getFieldName())
                            .append(") {")
                            .append("\r\t\tthis.")
                            .append(fieldUnit.getFieldName())
                            .append(" = ").append(fieldUnit.getFieldName())
                            .append(";")
                            .append(getLine(1))
                            .append("\t}");
                }
                if (toString) {
                    if (i > 0) {
                        toStringStr
                                .append(getLine(1))
                                .append(getTab(4))
                                .append("\", ");
                    } else {
                        toStringStr
                                .append(getTab(4))
                                .append("\"");
                    }
                    toStringStr
                            .append(fieldUnit.getFieldName())
                            .append("=")
                            .append(fieldUnit.getFieldType().javaType.equals(FieldTypeEnum.VARCHAR.javaType) ? "\'" : "")
                            .append("\" + ")
                            .append(fieldUnit.getFieldName())
                            .append(fieldUnit.getFieldType().javaType.equals(FieldTypeEnum.VARCHAR.javaType) ? " + \'\\\'\'" : "")
                            .append(" +");
                }
                if (allArgsConstructor) {
                    if (i > 0) {
                        allArgsConstructorStr
                                .append(", ");
                    }
                    allArgsConstructorStr
                            .append(fieldUnit.getFieldType().javaType)
                            .append(" ")
                            .append(fieldUnit.getFieldName());
                    allArgs
                            .append(getLine(1))
                            .append(getTab(2))
                            .append("this.")
                            .append(fieldUnit.getFieldName())
                            .append(" = ")
                            .append(fieldUnit.getFieldName())
                            .append(";");
                }
                if (equals) {
                    if (i > 0) {
                        equalsStr
                                .append(" &&")
                                .append(getLine(1))
                                .append(getTab(4));
                    }
                    equalsStr
                            .append("Objects.equals(")
                            .append(fieldUnit.getFieldName())
                            .append(", o1.")
                            .append(fieldUnit.getFieldName())
                            .append(")");
                }
                if (hashCode) {
                    if (i > 0) {
                        hashCodeStr.append(", ");
                    }
                    hashCodeStr.append(fieldUnit.getFieldName());
                }
                i++;
            }
            if (toString) {
                toStringStr
                        .append(getLine(1))
                        .append(getTab(4))
                        .append("\'}\';")
                        .append(getLine(1))
                        .append(getTab(1))
                        .append("}");
                result
                        .append(getLine(1))
                        .append(toStringStr);
            }
            if (noArgsConstructor) {
                result
                        .append(getLine(2))
                        .append(getTab(1))
                        .append("public ")
                        .append(classGenerateUnit.getClassName())
                        .append("() {")
                        .append(getLine(1))
                        .append(getTab(1))
                        .append("}");
            }
            if (allArgsConstructor) {
                allArgsConstructorStr
                        .append(") {")
                        .append(allArgs)
                        .append(getLine(1))
                        .append(getTab(1))
                        .append("}");
                result.append(allArgsConstructorStr);
            }
            if (equals) {
                equalsStr
                        .append(";")
                        .append(getLine(1))
                        .append(getTab(1))
                        .append("}");
                result
                        .append(getLine(1))
                        .append(equalsStr);
                classImportSet.add("java.util.Objects");
            }
            if (hashCode) {
                hashCodeStr
                        .append(");")
                        .append(getLine(1))
                        .append(getTab(1))
                        .append("}");
                result
                        .append(getLine(1))
                        .append(hashCodeStr);
                classImportSet.add("java.util.Objects");
            }
        }
        List<MethodUnit> methodUnits = classGenerateUnit.getMethodUnits();
        if (methodUnits == null || methodUnits.isEmpty()) {
            return result.toString();
        }
        for (MethodUnit methodUnit : methodUnits) {
            switch (methodUnit.getMethodType()) {
                case INTERFACE:
                    result
                            .append(getLine(1))
                            .append(getTab(1))
                            .append(StringUtils.isNotBlank(methodUnit.getResultParams()) ? methodUnit.getResultParams() : "void")
                            .append(" ")
                            .append(methodUnit.getMethodName());
                    List<String> methodParams = methodUnit.getMethodParams();
                    List<String> paramsNames = methodUnit.getMethodParamsName();
                    if (methodParams != null && !methodParams.isEmpty()) {
                        int count = 0;
                        result.append("(");
                        for (String methodParam : methodParams) {
                            String paramName = "record";
                            if (paramsNames != null && paramsNames.size() > count) {
                                String name = paramsNames.get(count);
                                if (StringUtils.isNotBlank(name)) {
                                    paramName = name;
                                }
                            }
                            if (count > 0) {
                                result
                                        .append(", ")
                                        .append(methodParam)
                                        .append(" ")
                                        .append(paramName)
                                        .append(count + 1);
                            } else {
                                result
                                        .append(methodParam)
                                        .append(" ")
                                        .append(paramName);
                            }
                            count++;
                        }
                        result.append(");").append(getLine(1));
                    } else {
                        result.append("();").append(getLine(1));
                    }
                    break;
                case ABSTRACT:
                    System.out.println("abstract...");
                    break;
            }
            Set<String> importPackages = methodUnit.getImportPackages();
            if (importPackages != null && !importPackages.isEmpty()) {
                classImportSet.addAll(importPackages);
            }
        }
        return result.toString();
    }

    /**
     * 初始化 hashCode方法
     *
     * @return hashCode
     */
    private StringBuilder initHashCodeStr() {
        return new StringBuilder()
                .append(getLine(1))
                .append(getTab(1))
                .append("@Override")
                .append(getLine(1))
                .append(getTab(1))
                .append("public int hashCode() {")
                .append(getLine(1))
                .append(getTab(2))
                .append("return Objects.hash(");
    }

    /**
     * 初始 equals 方法
     *
     * @return equals
     */
    private StringBuilder initEqualsStr() {
        return new StringBuilder()
                .append(getLine(1))
                .append(getTab(1))
                .append("@Override")
                .append(getLine(1))
                .append(getTab(1))
                .append("public boolean equals(Object o) {")
                .append(getLine(1))
                .append(getTab(2))
                .append("if (this == o) return true;")
                .append(getLine(1))
                .append(getTab(2))
                .append("if (o == null || getClass() != o.getClass()) return false;")
                .append(getLine(1))
                .append(getTab(2))
                .append(classGenerateUnit.getClassName())
                .append(" o1 = (")
                .append(classGenerateUnit.getClassName())
                .append(") o;")
                .append(getLine(1))
                .append(getTab(2))
                .append("return ");
    }

    /**
     * 初始化全参构造
     *
     * @return 全参构造
     */
    private StringBuilder initAllArgsConstructor() {
        return new StringBuilder()
                .append(getLine(2))
                .append(getTab(1))
                .append("public ")
                .append(classGenerateUnit.getClassName())
                .append("(");
    }

    /**
     * 初始化toString方法
     *
     * @return toString
     */
    private StringBuilder initToStringStr() {
        return new StringBuilder()
                .append(getLine(1))
                .append(getTab(1))
                .append("@Override")
                .append(getLine(1))
                .append(getTab(1))
                .append("public String toString() {")
                .append(getLine(1))
                .append(getTab(2))
                .append("return \"")
                .append(classGenerateUnit.getClassName())
                .append("{\" + ").append(getLine(1));
    }

    /**
     * 根据字段名获取getter方法方法名
     *
     * @param fieldName 字段名
     * @return 方法名
     */
    private String getGetterMethodName(String fieldName) {
        if (StringUtils.isNotBlank(fieldName)) {
            return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        }
        return null;
    }

    /**
     * 根据字段名获取setter方法方法名
     *
     * @param fieldName 字段名
     * @return 方法名
     */
    private String getSetterMethodName(String fieldName) {
        if (StringUtils.isNotBlank(fieldName)) {
            return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        }
        return null;
    }

    /**
     * 获取类字段
     *
     * @return 字段
     */
    private String getFields() {
        StringBuilder fields = new StringBuilder();
        List<FieldUnit> fieldUnits = classGenerateUnit.getFieldUnits();
        if (fieldUnits != null) {
            boolean isComment = classGenerateUnit.isComment();
            for (FieldUnit fieldUnit : fieldUnits) {
                if (isComment && StringUtils.isNotBlank(fieldUnit.getCommentText())) {
                    fields
                            .append("\r\t/**")
                            .append("\r\t * ").append(fieldUnit.getCommentText())
                            .append("\r\t */");
                }
                fields.append("\r\tprivate");
                if (fieldUnit.isStatic()) {
                    fields.append(" ").append("static");
                }
                if (fieldUnit.isFinal()) {
                    fields.append(" ").append("final");
                }
                fields.append(" ").append(fieldUnit.getFieldType().javaType);
                fields.append(" ").append(fieldUnit.getFieldName()).append(";").append(getLine(1));
                fieldImportSet.add(fieldUnit.getImportPackage());
            }
        }
        return fields.toString();
    }

    /**
     * 获取类
     *
     * @return 类
     */
    private String getClazz() {
        boolean serialize = classGenerateUnit.isSerialize();
        String result = "public "
                + classGenerateUnit.getClassType().toString().toLowerCase()
                + " "
                + classGenerateUnit.getClassName();
        if (serialize) {
            result += " implements Serializable {\r\n";
            result += "\r\tprivate static final long serialVersionUID = 1L;";
            fieldImportSet.add("java.io.Serializable");
        } else {
            result += " {";
        }
        return result;
    }

    /**
     * 拼接导入的包
     *
     * @return 字符串
     */
    private String getImports() {
        return getImports(annotationImportSet, 1) + getImports(classImportSet, 1) + getImports(fieldImportSet, 0);
    }

    private String getImports(Set<String> imports, int i) {
        StringBuilder importStr = new StringBuilder();
        for (String imp : imports) {
            if (imp != null) {
                if (i == 0) {
                    importStr.append(getLine(1));
                }
                importStr.append("import ").append(imp).append(";").append(getLine(1));
                i++;
            }
        }
        return importStr.toString();
    }

    /**
     * 获取包名
     *
     * @return 包名
     */
    private String getPackage() {
        String packageName = classGenerateUnit.getPackageName();
        if (StringUtils.isBlank(packageName)) {
            return null;
        }
        return "package " + packageName + ";";
    }

    /**
     * 获取文件生成名称和位置
     *
     * @return 文件
     */
    private String getGenerateFile() {
        StringBuilder generateFile = new StringBuilder();
        generateFile.append(classGenerateUnit.getGeneratePhysicsPath().replaceAll("\\\\", "/"));
        String packageName = classGenerateUnit.getPackageName();
        if (StringUtils.isNotBlank(packageName)) {
            generateFile
                    .append("/")
                    .append(packageName.replaceAll("\\.", "/"));
        }
        generateFile
                .append("/")
                .append(classGenerateUnit.getClassName())
                .append(".java");
        return generateFile.toString();
    }

    /**
     * 校验执行参数
     */
    private void checkExecuteParams() {
        if (classGenerateUnit == null) {
            throw new BaseException("生成内容为空！");
        }
        if (StringUtils.isBlank(classGenerateUnit.getGeneratePhysicsPath())) {
            throw new BaseException("找不到生成路径！");
        }
        if (classGenerateUnit.getClassType() == null) {
            throw new BaseException("未知的生成类型！");
        }
        if (StringUtils.isBlank(classGenerateUnit.getClassName())) {
            throw new BaseException("未指定类名！");
        }
    }
}
