package sll.plugin.helper.enums;

import sll.plugin.helper.model.ConnectionSettings;

/**
 * @ClassName LocalCacheEnum
 * @Description TODO
 * @Author LSL
 * @Date 2020/8/20 10:13
 * @Version 1.0
 * @Email lsl.yx@foxmail.com
 **/
public enum LocalCacheEnum {
    CONNECTION_SETTINGS_STATE(ConnectionSettings.class),
    GENERATOR_SETTINGS_STATE(Object.class);

    public Class<?> clazz;

    LocalCacheEnum(Class<?> clazz) {
        this.clazz = clazz;
    }
}
