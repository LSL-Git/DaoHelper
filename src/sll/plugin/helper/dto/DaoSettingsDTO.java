package sll.plugin.helper.dto;

/**
 * Created by LSL on 2020/1/15 12:23
 */
public class DaoSettingsDTO extends MapperSettingsDTO {

    /**
     * 添加Mapper注解
     */
    private boolean isMapper;

    /**
     * 添加Repository注解
     */
    private boolean isRepository;

    public boolean isMapper() {
        return isMapper;
    }

    public void setMapper(boolean mapper) {
        isMapper = mapper;
    }

    public boolean isRepository() {
        return isRepository;
    }

    public void setRepository(boolean repository) {
        isRepository = repository;
    }
}
