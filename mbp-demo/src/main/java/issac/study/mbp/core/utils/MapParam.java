package issac.study.mbp.core.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Map<String, Object> 的工具类
 *
 * @author issac.hu
 */
public class MapParam {

    private Map<String, Object> map = new HashMap<>();

    public static MapParam build() {
        return new MapParam();
    }

    /**
     * 将bean转换为MapParam
     *
     * @param bean
     * @return
     */
    public static MapParam build(Object bean) {
        MapParam mapParam = new MapParam();
        mapParam.map = ConvertUtils.toMap(bean);
        return mapParam;
    }

    public MapParam put(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

    public Object get(String key) {
        return this.map.get(key);
    }

    public MapParam remove(String key) {
        this.map.remove(key);
        return this;
    }

    public MapParam clear() {
        this.map.clear();
        return this;
    }

    public <T> T toBean(Class<T> tClass) {
        return ConvertUtils.convertObject(this.map, tClass);
    }

    public Map<String, Object> toMap() {
        return this.map;
    }

    public String toJsonString() {
        return ConvertUtils.toJsonString(this.map);
    }

}
