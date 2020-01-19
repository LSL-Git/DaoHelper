package sll.plugin.test;

import com.google.common.collect.ImmutableList;
import org.apache.commons.compress.utils.Sets;
import org.jetbrains.annotations.NotNull;
import sll.plugin.helper.dto.TableColumnDTO;
import sll.plugin.helper.generator.mybatis.generator.ClassGenerator;
import sll.plugin.helper.generator.unit.ClassGenerateUnit;
import sll.plugin.helper.generator.unit.base.AnnotationUnit;
import sll.plugin.helper.generator.unit.base.FieldUnit;
import sll.plugin.helper.generator.unit.base.MethodUnit;
import sll.plugin.helper.generator.unit.enums.ClassTypeEnum;
import sll.plugin.helper.generator.unit.enums.FieldTypeEnum;
import sll.plugin.helper.generator.unit.enums.LombokAnnotationEnum;
import sll.plugin.helper.generator.unit.enums.MethodTypeEnum;
import sll.plugin.helper.model.ConnectionSettings;
import sll.plugin.helper.utils.DatabaseUtil;

import java.util.*;

/**
 * Created by LSL on 2020/1/10 17:19
 */
public class test {

    public static void main(String[] args) throws Exception {
        ClassGenerateUnit generateUnit = new ClassGenerateUnit();
        generateUnit.setGeneratePhysicsPath("D:\\dev\\pro\\ktx\\tmp\\clazz");
        generateUnit.setPackageName("com.entity");
        generateUnit.setClassType(ClassTypeEnum.CLASS);
        generateUnit.setClassName("Demo");
        List<FieldUnit> fieldUnits = getFieldUnits();
        generateUnit.setFieldUnits(fieldUnits);
        generateUnit.setComment(true);
        generateUnit.setSerialize(true);
//        generateUnit.setToString(true);
//        generateUnit.setNoArgsConstructor(true);
//        generateUnit.setAllArgsConstructor(true);
//        generateUnit.setEquals(true);
//        generateUnit.setHashCode(true);
        generateUnit.setClassAnnotations(setAnnotations());
        generateUnit.setMethodUnits(getMethods());
        new ClassGenerator(generateUnit).execute();
        System.out.println("name".substring(0, 1).toUpperCase()+"name".substring(1));
        String s = "public void %s {";
        System.out.println(String.format(s, "get"));
        System.out.println("ss" + 'd');

//        getTableColumns();
        LombokAnnotationEnum enumByTxt = LombokAnnotationEnum.getEnumByTxt("@Data");
        System.out.println(enumByTxt);

        FieldTypeEnum varchar = FieldTypeEnum.getEnum("varchar");
        System.out.println(varchar);
    }

    private static void getTableColumns() throws Exception {
        ConnectionSettings connectionSettings = new ConnectionSettings();
        connectionSettings.setHost("127.0.0.1");
        connectionSettings.setPort("3306");
        connectionSettings.setDatabase("ktx_v2");
        connectionSettings.setUsername("root");
        connectionSettings.setPassword("ktx777552");
        DatabaseUtil databaseUtil = new DatabaseUtil(connectionSettings);
        Map<String, List<TableColumnDTO>> tableColumns = databaseUtil.getTableColumns(ImmutableList.of("base_user", "user_room", "order_pay"));
        System.out.println(tableColumns);
    }

    private static List<MethodUnit> getMethods() {
        List<MethodUnit> methodUnits = new ArrayList<>();
        MethodUnit methodUnit = new MethodUnit();
        methodUnit.setMethodType(MethodTypeEnum.INTERFACE);
        methodUnit.setMethodName("insert");
        methodUnit.setResultParams("int");
        methodUnit.setMethodParams(ImmutableList.of("Test", "Integer"));
        methodUnit.setImportPackages(Sets.newHashSet("com.entity.Test"));
        methodUnits.add(methodUnit);
        methodUnit = new MethodUnit();
        methodUnit.setMethodType(MethodTypeEnum.INTERFACE);
        methodUnit.setMethodName("update");
        methodUnit.setResultParams("int");
        methodUnit.setMethodParams(ImmutableList.of("Test"));
        methodUnit.setImportPackages(Sets.newHashSet("com.entity.Test"));
        methodUnits.add(methodUnit);

        methodUnit = new MethodUnit();
        methodUnit.setMethodType(MethodTypeEnum.INTERFACE);
        methodUnit.setMethodName("getOne");
        methodUnit.setResultParams("int");
        methodUnits.add(methodUnit);

        methodUnit = new MethodUnit();
        methodUnit.setMethodName("getOne");
        methodUnit.setResultParamName("one");
        methodUnit.setMethodType(MethodTypeEnum.INTERFACE);
        methodUnit.setResultParams("int");
        methodUnits.add(methodUnit);

        methodUnit = new MethodUnit();
        methodUnit.setMethodName("getOne1");
        methodUnit.setResultParamName("one1");
        methodUnit.setMethodType(MethodTypeEnum.ABSTRACT);
        methodUnit.setResultParams("Integer");
        methodUnits.add(methodUnit);
        return methodUnits;
    }

    private static List<AnnotationUnit> setAnnotations() {
        List<AnnotationUnit> annotationUnits = new ArrayList<>();
        AnnotationUnit annotationUnit = new AnnotationUnit();
        annotationUnit.setAnnotationName("@Data");
        annotationUnit.setImportPackage("lombok.Data");
        annotationUnits.add(annotationUnit);
        annotationUnit = new AnnotationUnit();
        annotationUnit.setAnnotationName("@ToString");
        annotationUnit.setImportPackage("lombok.ToString");
        annotationUnits.add(annotationUnit);
        return annotationUnits;
    }

    @NotNull
    private static List<FieldUnit> getFieldUnits() {
        List<FieldUnit> fieldUnits = new ArrayList<>();
        FieldUnit e = new FieldUnit();
        e.setFieldName("id");
        e.setCommentText("id");
        e.setGetter(true);
        e.setSetter(true);
        e.setFieldType(FieldTypeEnum.INT);
        fieldUnits.add(e);

        e = new FieldUnit();
        e.setFieldName("name");
        e.setCommentText("名字");
        e.setGetter(true);
        e.setSetter(true);
        e.setFieldType(FieldTypeEnum.INT);
        fieldUnits.add(e);

        e = new FieldUnit();
        e.setFieldName("age");
        e.setCommentText("年龄");
        e.setFieldType(FieldTypeEnum.DATETIME);
        e.setGetter(true);
        e.setSetter(true);
        e.setImportPackage("java.util.Date");
        fieldUnits.add(e);

        e = new FieldUnit();
        e.setFieldName("address");
        e.setCommentText("地址");
        e.setFieldType(FieldTypeEnum.VARCHAR);
        e.setGetter(true);
        e.setSetter(true);
        fieldUnits.add(e);

        e = new FieldUnit();
        e.setFieldName("gender");
        e.setCommentText("性别");
        e.setFieldType(FieldTypeEnum.INT);
        e.setGetter(true);
        e.setSetter(true);
        fieldUnits.add(e);
//        e = new FieldUnit();
//        e.setFieldName("staticTest");
//        e.setCommentText("静态");
//        e.setFieldType(FieldTypeEnum.INT);
//        e.setStatic(true);
//        fieldUnits.add(e);
//        e = new FieldUnit();
//        e.setFieldName("finalTest");
//        e.setCommentText("常量");
//        e.setFieldType(FieldTypeEnum.INT);
//        e.setFinal(true);
//        fieldUnits.add(e);
//        e = new FieldUnit();
//        e.setFieldName("finalStaticTest");
//        e.setCommentText("静态常量");
//        e.setFieldType(FieldTypeEnum.INT);
//        e.setFinal(true);
//        e.setStatic(true);
//        fieldUnits.add(e);
        return fieldUnits;
    }
}
