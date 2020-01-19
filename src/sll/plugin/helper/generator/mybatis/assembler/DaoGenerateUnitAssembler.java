package sll.plugin.helper.generator.mybatis.assembler;

import org.apache.commons.lang.StringUtils;
import sll.plugin.helper.dto.DaoSettingsDTO;
import sll.plugin.helper.exception.BaseException;
import sll.plugin.helper.generator.unit.ClassGenerateUnit;
import sll.plugin.helper.generator.unit.base.AnnotationUnit;
import sll.plugin.helper.generator.unit.base.FieldUnit;
import sll.plugin.helper.generator.unit.base.MethodUnit;
import sll.plugin.helper.generator.unit.enums.ClassTypeEnum;
import sll.plugin.helper.generator.unit.enums.MapperMethodEnum;
import sll.plugin.helper.generator.unit.enums.MethodTypeEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by LSL on 2020/1/15 18:38
 */
public class DaoGenerateUnitAssembler {

    private List<ClassGenerateUnit> entityGenerateUnitList;

    private DaoSettingsDTO daoSettingsDTO;


    public DaoGenerateUnitAssembler(List<ClassGenerateUnit> entityGenerateUnitList, DaoSettingsDTO daoSettingsDTO) {
        this.entityGenerateUnitList = entityGenerateUnitList;
        this.daoSettingsDTO = daoSettingsDTO;
    }

    public List<ClassGenerateUnit> execute() {
        if (entityGenerateUnitList == null || daoSettingsDTO == null) {
            throw new BaseException("生成dao信息缺失！");
        }

        List<ClassGenerateUnit> daoGenerateUnitList = new ArrayList<>();
        for (ClassGenerateUnit entityGenerateUnit : entityGenerateUnitList) {
            ClassGenerateUnit classGenerateUnit = new ClassGenerateUnit();
            classGenerateUnit.setGeneratePhysicsPath(daoSettingsDTO.getParentPath() + "/" + daoSettingsDTO.getRelativePath());
            classGenerateUnit.setPackageName(daoSettingsDTO.getPackageName());
            classGenerateUnit.setClassType(ClassTypeEnum.INTERFACE);
            classGenerateUnit.setTableName(entityGenerateUnit.getTableName());
            // 拼接类名
            classGenerateUnit.setClassName(entityGenerateUnit.getClassName() + "Dao");
            if (daoSettingsDTO.isMapper()) {
                // 添加注解相关信息
                AnnotationUnit annotationUnit = new AnnotationUnit();
                annotationUnit.setImportPackage("org.apache.ibatis.annotations.Mapper");
                annotationUnit.setAnnotationName("@Mapper");
                classGenerateUnit.setClassAnnotations(Collections.singletonList(annotationUnit));
                // 拼接类名
                classGenerateUnit.setClassName(entityGenerateUnit.getClassName() + "Mapper");
            }
            if (daoSettingsDTO.isRepository()) {
                // 添加注解相关信息
                AnnotationUnit annotationUnit = new AnnotationUnit();
                annotationUnit.setImportPackage("org.springframework.stereotype.Repository");
                annotationUnit.setAnnotationName("@Repository");
                classGenerateUnit.setClassAnnotations(Collections.singletonList(annotationUnit));
            }
            // 拼接接口方法
            classGenerateUnit.setMethodUnits(setMethodUnits(entityGenerateUnit));
            daoGenerateUnitList.add(classGenerateUnit);
        }
        return daoGenerateUnitList;
    }

    /**
     * 组装dao类接口
     *
     * @param generateUnit 信息
     * @return 方法列表
     */
    private List<MethodUnit> setMethodUnits(ClassGenerateUnit generateUnit) {
        List<String> methods = daoSettingsDTO.getMethods();
        if (methods != null && !methods.isEmpty()) {
            List<MethodUnit> list = new ArrayList<>();
            for (String method : methods) {
                MethodUnit methodUnit = new MethodUnit();
                methodUnit.setMethodType(MethodTypeEnum.INTERFACE);
                methodUnit.setMethodName(method);
                if (StringUtils.isNotBlank(generateUnit.getPackageName())
                        && !generateUnit.getPackageName().equals(daoSettingsDTO.getPackageName())) {
                    methodUnit.setImportPackages(Collections.singleton(generateUnit.getPackageName() + "." + generateUnit.getClassName()));
                }
                // insert, insertSelective, update, updateSelective
                if (method.equals(MapperMethodEnum.INSERT.txt)
                        || method.equals(MapperMethodEnum.INSERT_SELECTIVE.txt)
                        || method.equals(MapperMethodEnum.UPDATE.txt)
                        || method.equals(MapperMethodEnum.UPDATE_SELECTIVE.txt)) {
                    methodUnit.setMethodParams(Collections.singletonList(generateUnit.getClassName()));
                    methodUnit.setResultParams("int");
                }
                // queryOne 通过主键查询
                if (method.equals(MapperMethodEnum.QUERY_ONE.txt)) {
                    methodUnit.setResultParams(generateUnit.getClassName());
                    FieldUnit primaryKey = getPrimaryKeyField(generateUnit.getFieldUnits());
                    if (primaryKey == null) {
                        System.out.println("queryOne方法未找到主键列");
                        continue;
                    }
                    methodUnit.setMethodParams(Collections.singletonList(primaryKey.getFieldType().javaType));
                    methodUnit.setMethodParamsName(Collections.singletonList(primaryKey.getFieldName()));
                }
                // delete
                if (method.equals(MapperMethodEnum.DELETE.txt)) {
                    methodUnit.setResultParams("int");
                    FieldUnit primaryKey = getPrimaryKeyField(generateUnit.getFieldUnits());
                    if (primaryKey == null) {
                        System.out.println("delete方法未找到主键列");
                        continue;
                    }
                    methodUnit.setMethodParams(Collections.singletonList(primaryKey.getFieldType().javaType));
                    methodUnit.setMethodParamsName(Collections.singletonList(primaryKey.getFieldName()));
                }
                list.add(methodUnit);
            }
            return list;
        }
        return null;
    }

    /**
     * 获取主键字段
     *
     * @param fieldUnits 字段列表
     * @return 字段
     */
    private FieldUnit getPrimaryKeyField(List<FieldUnit> fieldUnits) {
        if (fieldUnits != null && !fieldUnits.isEmpty()) {
            for (FieldUnit fieldUnit : fieldUnits) {
                if (fieldUnit.isPrimaryKey()) {
                    return fieldUnit;
                }
            }
        }
        return null;
    }

    private DaoGenerateUnitAssembler() {
    }
}
