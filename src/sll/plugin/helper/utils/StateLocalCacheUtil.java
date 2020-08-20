package sll.plugin.helper.utils;

import com.alibaba.fastjson.JSON;
import com.intellij.ide.util.PropertiesComponent;
import org.apache.commons.lang.StringUtils;
import sll.plugin.helper.enums.LocalCacheEnum;

import java.lang.reflect.Type;

/**
 * 状态本地缓存
 *
 * @ClassName StateCacheUtil
 * @Description TODO
 * @Author LSL
 * @Date 2020/8/19 18:34
 * @Version 1.0
 * @Email lsl.yx@foxmail.com
 **/
public abstract class StateLocalCacheUtil {

    /**
     * 设置缓存数据
     *
     * @param key   键
     * @param value 存储的值
     */
    public static void set(LocalCacheEnum key, Object value) {
        if (value == null) {
            return;
        }
        PropertiesComponent.getInstance().setValue(key.name(), JSON.toJSONString(value));
    }

    /**
     * 获取缓存对象
     *
     * @param key   键
     */
    public static <T> T get(LocalCacheEnum key) {
        String value = PropertiesComponent.getInstance().getValue(key.name());
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return JSON.parseObject(value, (Type) key.clazz);
    }
}
