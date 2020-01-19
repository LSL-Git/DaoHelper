package sll.plugin.helper.generator.mybatis;

import sll.plugin.helper.dto.*;
import sll.plugin.helper.exception.BaseException;
import sll.plugin.helper.generator.mybatis.assembler.DaoGenerateUnitAssembler;
import sll.plugin.helper.generator.mybatis.assembler.EntityGenerateUnitAssembler;
import sll.plugin.helper.generator.mybatis.assembler.XmlGenerateUnitAssembler;
import sll.plugin.helper.generator.mybatis.generator.ClassGenerator;
import sll.plugin.helper.generator.mybatis.generator.XmlGenerator;
import sll.plugin.helper.generator.unit.ClassGenerateUnit;
import sll.plugin.helper.generator.unit.XmlGenerateUnit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Map;

/**
 * 主生成任务控制
 * <p>
 * Created by LSL on 2020/1/15 9:55
 */
public class GenerateMainTask {

    /**
     * entity 生成配置
     */
    private EntitySettingsDTO entitySettingsDTO;

    /**
     * dao 生成配置
     */
    private DaoSettingsDTO daoSettingsDTO;

    /**
     * mapper 生成配置
     */
    private MapperSettingsDTO mapperSettingsDTO;

    /**
     * 表对应列信息
     */
    private Map<String, List<TableColumnDTO>> columnsMap;

    public GenerateMainTask(GenerateMainTaskDTO generateMainTaskDTO) {
        if (generateMainTaskDTO == null) {
            throw new BaseException("主生成数据缺失！");
        }
        this.entitySettingsDTO = generateMainTaskDTO.getEntitySettingsDTO();
        this.daoSettingsDTO = generateMainTaskDTO.getDaoSettingsDTO();
        this.mapperSettingsDTO = generateMainTaskDTO.getMapperSettingsDTO();
        this.columnsMap = generateMainTaskDTO.getColumnsMap();
    }

    public String execute() {
        long startTime = System.currentTimeMillis();
        EntityGenerateUnitAssembler entityUnitAssembler = new EntityGenerateUnitAssembler(entitySettingsDTO, columnsMap);
        List<ClassGenerateUnit> entityGenerateUnitList = entityUnitAssembler.execute();
        System.out.println("[INFO] ------------------------- GENERATE ENTITY FILES ---------------------------");
        for (ClassGenerateUnit classGenerateUnit : entityGenerateUnitList) {
            new ClassGenerator(classGenerateUnit).execute();
        }
        System.out.println("[INFO] ---------------------------------------------------------------------------");
        System.out.println("[INFO] ------------------------- GENERATE DAO FILES ------------------------------");
        DaoGenerateUnitAssembler daoGenerateUnitAssembler = new DaoGenerateUnitAssembler(entityGenerateUnitList, daoSettingsDTO);
        List<ClassGenerateUnit> daoGenerateUnitList = daoGenerateUnitAssembler.execute();
        for (ClassGenerateUnit classGenerateUnit : daoGenerateUnitList) {
            new ClassGenerator(classGenerateUnit).execute();
        }
        System.out.println("[INFO] ---------------------------------------------------------------------------");
        System.out.println("[INFO] ------------------------- GENERATE MAPPER FILES ---------------------------");
        XmlGenerateUnitAssembler xmlGenerateUnitAssembler = new XmlGenerateUnitAssembler(entityGenerateUnitList, daoGenerateUnitList, mapperSettingsDTO);
        List<XmlGenerateUnit> xmlGenerateUnitList = xmlGenerateUnitAssembler.execute();
        for (XmlGenerateUnit xmlGenerateUnit : xmlGenerateUnitList) {
            new XmlGenerator(xmlGenerateUnit).execute();
        }
        long endTime = System.currentTimeMillis();
        // 总耗时
        float elapsedTime = (endTime - startTime) / 1000f;
        System.out.println("[INFO] ---------------------------------------------------------------------------");
        System.out.println("[INFO] GENERATE SUCCESS");
        System.out.println("[INFO] ---------------------------------------------------------------------------");
        System.out.println("[INFO] Total time：" + elapsedTime + " s");
        System.out.println("[INFO] ---------------------------------------------------------------------------");
        return elapsedTime + " s";
    }
}
