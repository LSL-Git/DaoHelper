package sll.plugin.helper.generator.mybatis.assembler;


import org.apache.commons.lang.StringUtils;
import sll.plugin.helper.dto.MapperSettingsDTO;
import sll.plugin.helper.exception.BaseException;
import sll.plugin.helper.generator.unit.ClassGenerateUnit;
import sll.plugin.helper.generator.unit.XmlGenerateUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * mapper xml 生成单元组装器
 * <p>
 * Created by LSL on 2020/1/16 16:42
 */
public class XmlGenerateUnitAssembler {

    private List<ClassGenerateUnit> entityGenerateUnitList;

    private List<ClassGenerateUnit> daoGenerateUnitList;

    private MapperSettingsDTO mapperSettingsDTO;

    private Map<String, String> daoPackageMap;

    public XmlGenerateUnitAssembler(List<ClassGenerateUnit> entityGenerateUnitList,
                                    List<ClassGenerateUnit> daoGenerateUnitList,
                                    MapperSettingsDTO mapperSettingsDTO) {
        this.entityGenerateUnitList = entityGenerateUnitList;
        this.daoGenerateUnitList = daoGenerateUnitList;
        this.mapperSettingsDTO = mapperSettingsDTO;
    }

    public List<XmlGenerateUnit> execute() {
        if (entityGenerateUnitList == null || daoGenerateUnitList == null || mapperSettingsDTO == null) {
            throw new BaseException("生成mapper信息缺失！");
        }

        initDaoMap();

        List<XmlGenerateUnit> xmlGenerateUnits = new ArrayList<>();
        if (!entityGenerateUnitList.isEmpty()) {
            for (ClassGenerateUnit classGenerateUnit : entityGenerateUnitList) {
                XmlGenerateUnit xmlGenerateUnit = new XmlGenerateUnit();
                xmlGenerateUnit.setGeneratePhysicsPath(mapperSettingsDTO.getParentPath() + "/" + mapperSettingsDTO.getRelativePath());
                xmlGenerateUnit.setPackageName(mapperSettingsDTO.getPackageName());
                xmlGenerateUnit.setXmlName(classGenerateUnit.getClassName() + "Mapper");
                xmlGenerateUnit.setNameSpace(daoPackageMap.get(classGenerateUnit.getTableName()));
                if (StringUtils.isNotBlank(classGenerateUnit.getPackageName())) {
                    xmlGenerateUnit.setResultType(classGenerateUnit.getPackageName() + "." + classGenerateUnit.getClassName());
                } else {
                    xmlGenerateUnit.setResultType(classGenerateUnit.getClassName());
                }
                xmlGenerateUnit.setFieldUnits(classGenerateUnit.getFieldUnits());
                xmlGenerateUnit.setMethods(mapperSettingsDTO.getMethods());
                xmlGenerateUnit.setTableName(classGenerateUnit.getTableName());

                xmlGenerateUnits.add(xmlGenerateUnit);
            }
        }

        return xmlGenerateUnits;
    }

    /**
     * 初始化表名与dao接口的关系
     */
    private void initDaoMap() {
        if (!daoGenerateUnitList.isEmpty()) {
            daoPackageMap = new HashMap<>();
            for (ClassGenerateUnit classGenerateUnit : daoGenerateUnitList) {
                if (StringUtils.isNotBlank(classGenerateUnit.getPackageName())) {
                    daoPackageMap.put(classGenerateUnit.getTableName(), classGenerateUnit.getPackageName() + "." + classGenerateUnit.getClassName());
                } else {
                    daoPackageMap.put(classGenerateUnit.getTableName(), classGenerateUnit.getClassName());
                }
            }
        }
    }

    private XmlGenerateUnitAssembler() {
    }
}
