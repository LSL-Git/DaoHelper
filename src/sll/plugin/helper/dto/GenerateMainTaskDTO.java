package sll.plugin.helper.dto;

import java.util.List;
import java.util.Map;

/**
 * Created by LSL on 2020/1/15 12:31
 */
public class GenerateMainTaskDTO {

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

    public MapperSettingsDTO getMapperSettingsDTO() {
        return mapperSettingsDTO;
    }

    public void setMapperSettingsDTO(MapperSettingsDTO mapperSettingsDTO) {
        this.mapperSettingsDTO = mapperSettingsDTO;
    }

    public EntitySettingsDTO getEntitySettingsDTO() {
        return entitySettingsDTO;
    }

    public void setEntitySettingsDTO(EntitySettingsDTO entitySettingsDTO) {
        this.entitySettingsDTO = entitySettingsDTO;
    }

    public DaoSettingsDTO getDaoSettingsDTO() {
        return daoSettingsDTO;
    }

    public void setDaoSettingsDTO(DaoSettingsDTO daoSettingsDTO) {
        this.daoSettingsDTO = daoSettingsDTO;
    }

    public Map<String, List<TableColumnDTO>> getColumnsMap() {
        return columnsMap;
    }

    public void setColumnsMap(Map<String, List<TableColumnDTO>> columnsMap) {
        this.columnsMap = columnsMap;
    }
}
