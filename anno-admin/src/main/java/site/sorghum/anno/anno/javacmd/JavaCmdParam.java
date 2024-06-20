package site.sorghum.anno.anno.javacmd;

import cn.hutool.core.convert.Convert;
import site.sorghum.anno._common.util.JSONUtil;

import java.util.HashMap;

/**
 * Java命令行参数
 *
 * @author Sorghum
 * @since 2024/06/20
 */
public class JavaCmdParam extends HashMap<String, Object> {

    /**
     * 将指定键对应的值转换为指定类型的对象
     *
     * @param key 键值
     * @param clazz 目标对象的类型
     * @param <T> 目标对象的泛型类型
     * @return 转换后的对象，如果转换失败则返回null
     */
    public <T>  T toT(Class<T> clazz) {
        return JSONUtil.toBean(this,clazz);
    }

    /**
     * 获取额外的输入参数
     *
     * @return 返回一个包含额外输入参数的Map，其中键为字符串类型，值为对象类型
     */
    public JavaCmdParam getExtraInput() {
        return JSONUtil.toBean(get("_extra"), JavaCmdParam.class);
    }

    /**
     * 获取指定键对应的字符串值
     *
     * @param key 键值
     * @return 返回对应键的字符串值，若键不存在则返回null
     */
    public String getString(String key) {
        return Convert.toStr(get(key));
    }

    /**
     * 根据给定的键获取对应的整数值
     *
     * @param key 键值
     * @return 对应的整数值，如果转换失败则返回null
     */
    public Integer getInteger(String key) {
        return Convert.toInt(get(key));
    }

    /**
     * 获取指定键对应的浮点数
     * @param key 键值
     * @return 对应的浮点数，如果转换失败则返回null
     */
    public Long getLong(String key) {
        return Convert.toLong(get(key));
    }

    /**
     * 获取指定键对应的布尔值
     * @param key 键值
     * @return 对应的布尔值，如果转换失败则返回null
     */
    public Boolean getBoolean(String key) {
        return Convert.toBool(get(key));
    }



}
