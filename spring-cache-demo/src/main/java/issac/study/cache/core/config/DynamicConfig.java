package issac.study.cache.core.config;

import com.alibaba.fastjson.JSON;
import issac.study.cache.exception.BusinessRuntimeException;
import issac.study.cache.model.ConfigEntity;
import issac.study.cache.repository.ConfigRepository;
import issac.study.cache.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author issac.hu
 * 从主数据库获取config表的配置key-value信息
 */
@Service
public class DynamicConfig {

    public static final String GET = "get";
    public static final String SET = "set";
    private static Logger logger = LoggerFactory.getLogger(DynamicConfig.class);

    private static ConfigRepository configRepository;

    private static Environment environment;

    private static final ConcurrentHashMap<String, String> CONFIG_CACHE = new ConcurrentHashMap<>();

    private static final String CONFIG_CACHE_DELIMETER = "<==>";

    private static final ConcurrentHashMap<String, ProxyHolder> PROXY_CACHE = new ConcurrentHashMap<>();

    @Autowired
    public void setConfigRepository(ConfigRepository configRepository) {
        DynamicConfig.configRepository = configRepository;
        refresh();
    }

    /**
     * 定时，获取数据库配置到缓存
     */
    @Scheduled(fixedRate = 60000)
    public void refresh() {
        List<ConfigEntity> configEntityList = configRepository.findAll();
        logger.debug("refresh configEntityList:{}", JSON.toJSONString(configEntityList));
        for (ConfigEntity configEntity : configEntityList) {
            if (StringUtils.isNotBlank(configEntity.getCKey()) && StringUtils.isNotBlank(configEntity.getCVal())) {
                String key = getConfigCacheKey(configEntity.getCKey().trim());
                CONFIG_CACHE.put(key, configEntity.getCVal().trim());
            }
        }
    }

    @Autowired
    public void setEnvironment(Environment environment) {
        DynamicConfig.environment = environment;
    }

    public static String getConfigCacheKey(String key) {
        return key;
    }

    /**
     * 直接从缓存中获取值
     *
     * @param key
     * @return
     */
    public static String get(String key) {
        return CONFIG_CACHE.get(key);
    }

    /**
     * 直接从缓存中获取值，如果没有的话返回defaultValue
     *
     * @param key
     * @param defaultValue
     * @param <T>
     * @return
     */
    public static <T> T get(String key, T defaultValue) {
        Objects.requireNonNull(defaultValue);
        return get(key, false, defaultValue, null);
    }


    /**
     * 从数据库的缓存中获取值，如果获取不到从命令行参数中获取，如果还是获取不到，从spring配置中获取，如果还获取不到返回默认值
     *
     * @param key
     * @param fetchSpringConfigWhenCacheIsNull
     * @param defaultValue
     * @param tClass                           defaultValue的类型，如果默认值为null那么就取tClass的类型
     * @param <T>
     * @return
     */
    public static <T> T get(String key, boolean fetchSpringConfigWhenCacheIsNull, T defaultValue, Class tClass) {
        String configCacheKey = getConfigCacheKey(key);
        String cache = DynamicConfig.CONFIG_CACHE.get(configCacheKey);
        Class clazz = tClass;
        if (defaultValue != null) {
            clazz = defaultValue.getClass();
        }

        try {
            //如果数据库有值，那么从数据库的缓存获取
            if (cache != null) {
                Object result = resolveObj(cache, clazz);
                return (T) result;
            } else {
                //从命令行获取
                cache = getFromCommandLine(key);
                if (cache != null) {
                    Object result = resolveObj(cache, clazz);
                    return (T) result;
                }
            }
        } catch (Exception e) {
            throw BusinessRuntimeException.error("[Config]配置表[" + key + "->" + cache + "]值配置类型错误:" + e);
        }
        if (fetchSpringConfigWhenCacheIsNull) {
            Object springValue = environment.getProperty(key, clazz);
            if (springValue != null) {
                return (T) springValue;
            }
        }
        return defaultValue;
    }

    private static String getFromCommandLine(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        String property = System.getProperty(key);
        if (StringUtils.isNotBlank(property)) {
            return property;
        }
        return null;
    }

    private static Object resolveObj(String cache, Class clazz) {
        if (cache == null) {
            return null;
        }
        Object obj;
        if (Integer.class.isAssignableFrom(clazz)) {
            obj = Integer.parseInt(cache);
        } else if (Long.class.isAssignableFrom(clazz)) {
            obj = Long.parseLong(cache);
        } else if (Boolean.class.isAssignableFrom(clazz)) {
            obj = Boolean.parseBoolean(cache);
        } else if (Double.class.isAssignableFrom(clazz)) {
            obj = Double.parseDouble(cache);
        } else if (Float.class.isAssignableFrom(clazz)) {
            obj = Float.parseFloat(cache);
        } else if (BigDecimal.class.isAssignableFrom(clazz)) {
            obj = BigDecimal.valueOf(Double.parseDouble(cache));
        } else if (Date.class.isAssignableFrom(clazz)) {
            obj = DateUtils.toDate(cache);
        } else if (LocalDateTime.class.isAssignableFrom(clazz)) {
            obj = DateUtils.toLocalDateTime(cache);
        } else {
            obj = cache;
        }
        return obj;
    }


    private static String convertObjToStr(Object val) {
        if (val == null) {
            return null;
        }
        String result;
        if (val instanceof Date) {
            result = DateUtils.parse((Date) val);
        } else if (val instanceof LocalDateTime) {
            result = DateUtils.parse((LocalDateTime) val);
        } else {
            result = String.valueOf(val);
        }
        return result;
    }

    /**
     * 产生配置的bean
     * 获取顺序：
     * 首先从数据库查找@FieldKey上的定义的值，如果为空
     * 从配置文件中找，如果还是位空,那么直接放回字段的默认值
     *
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T get(Class<T> tClass) {
        String tClassName = tClass.getName();
        ProxyHolder proxyHolder = PROXY_CACHE.get(tClassName);
        if (proxyHolder != null) {
            return (T) proxyHolder.getProxy();
        }
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(tClass);
        enhancer.setCallback((MethodInterceptor) (object, method, args, methodProxy) -> {
            String methodName = method.getName();
            Object result = null;
            if (methodName.startsWith(GET)) {
                String rawName = methodName.replaceFirst(GET, "");
                if (StringUtils.isNotBlank(rawName)) {
                    String fieldName = StringUtils.uncapitalize(rawName);
                    Field field = ReflectionUtils.findField(tClass, fieldName);
                    if (field != null) {
                        ConfigKey configKey = field.getAnnotation(ConfigKey.class);
                        if (configKey != null && StringUtils.isNotBlank(configKey.value())) {
                            String key = configKey.value();
                            field.setAccessible(true);
                            result = get(key, true, field.get(object), field.getType());
                        }
                    }
                }
            }
            return result;
        });
        PROXY_CACHE.putIfAbsent(tClassName, new ProxyHolder(enhancer.create()));
        return (T) PROXY_CACHE.get(tClassName).getProxy();
    }

    /**
     * 快捷方式
     *
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T getFromDb(Class<T> tClass) {
        return getFromDb(tClass, true);
    }

    /**
     * 直接从数据库读取
     *
     * @param tClass
     * @param setKeyAndValIfAbsent
     * @param <T>
     * @return
     */
    public static <T> T getFromDb(Class<T> tClass, boolean setKeyAndValIfAbsent) {
        String tClassName = tClass.getName() + "_db";
        ProxyHolder proxyHolder = PROXY_CACHE.get(tClassName);
        if (proxyHolder != null) {
            return (T) proxyHolder.getProxy();
        }
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(tClass);
        enhancer.setCallback((MethodInterceptor) (object, method, args, methodProxy) -> {
            String methodName = method.getName();
            Object result = null;
            if (methodName.startsWith(GET)) {
                String rawName = methodName.replaceFirst(GET, "");
                if (StringUtils.isNotBlank(rawName)) {
                    String fieldName = StringUtils.uncapitalize(rawName);
                    Field field = ReflectionUtils.findField(tClass, fieldName);
                    if (field != null) {
                        ConfigKey configKey = field.getAnnotation(ConfigKey.class);
                        if (configKey != null && StringUtils.isNotBlank(configKey.value())) {
                            String key = configKey.value();
                            ConfigEntity byCKey = configRepository.findBycKey(key);
                            if (byCKey != null && byCKey.getCVal() != null) {
                                result = resolveObj(byCKey.getCVal(), field.getType());
                            }
                            if (result == null) {
                                field.setAccessible(true);
                                return field.get(object);
                            }
                        }
                    }
                }
            } else if (methodName.startsWith(SET)) {
                String rawName = methodName.replaceFirst(SET, "");
                if (StringUtils.isNotBlank(rawName)) {
                    String fieldName = StringUtils.uncapitalize(rawName);
                    Field field = ReflectionUtils.findField(tClass, fieldName);
                    if (field != null) {
                        ConfigKey configKey = field.getAnnotation(ConfigKey.class);
                        String desc = configKey.desc();
                        if (configKey != null && StringUtils.isNotBlank(configKey.value())) {
                            String key = configKey.value();
                            if (args.length > 0) {
                                ConfigEntity byCKey = configRepository.findBycKey(key);
                                String cVal = convertObjToStr(args[0]);
                                if (byCKey != null) {
                                    byCKey.setCVal(cVal);
                                    configRepository.save(byCKey);
                                } else {
                                    if (setKeyAndValIfAbsent && StringUtils.isNotBlank(desc)) {
                                        ConfigEntity configEntity = new ConfigEntity();
                                        configEntity.setCDesc(desc);
                                        configEntity.setCKey(key);
                                        configEntity.setCVal(cVal);
                                        configRepository.save(configEntity);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return result;
        });
        PROXY_CACHE.putIfAbsent(tClassName, new ProxyHolder(enhancer.create()));
        return (T) PROXY_CACHE.get(tClassName).getProxy();
    }

    private static class ProxyHolder {
        Object proxy;

        public ProxyHolder(Object proxy) {
            this.proxy = proxy;
        }

        public Object getProxy() {
            return proxy;
        }

        public void setProxy(Object proxy) {
            this.proxy = proxy;
        }
    }


}
