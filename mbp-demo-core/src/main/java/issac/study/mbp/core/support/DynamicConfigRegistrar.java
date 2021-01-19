package issac.study.mbp.core.support;

import issac.study.mbp.core.annotation.DConf;
import issac.study.mbp.core.annotation.EnableDConf;
import issac.study.mbp.core.builder.DConfBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Configuration注解上如果有@import的注解或父类被@import的注解 见:{@link issac.study.mbp.core.annotation.EnableDConf}，会进入此类
 *
 * @author issac.hu
 */
@Slf4j
public class DynamicConfigRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware, ResourceLoaderAware {
    static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
    private String resourcePattern = DEFAULT_RESOURCE_PATTERN;
    private Environment environment;
    private ResourcePatternResolver resourcePatternResolver;
    private MetadataReaderFactory metadataReaderFactory;

    /**
     * 这个方法没被@import一次，就会被调用一次，也即被@EnableDConf注解的@configuration配置类，会被调用多次
     *
     * @param importingClassMetadata
     * @param registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //获取EnableDConf的属性
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(EnableDConf.class.getName());
        String[] basePackages = (String[]) annotationAttributes.get("value");
        if (basePackages.length == 0) {
            basePackages = (String[]) annotationAttributes.get("basePackages");
        }
        if (basePackages.length == 0) {
            //获取被注解类的名称，即@configuration类的包名称
            String className = importingClassMetadata.getClassName();
            int lastIndexOf = className.lastIndexOf(".");
            String classPackage = className.substring(0, lastIndexOf);
            basePackages = new String[]{classPackage};
        }
        //扫描@DConf注解
        Set<Class> candidateClasses = scan(basePackages);
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) registry;
        for (Class candidateClass : candidateClasses) {
            // singletonBeanRegistry.registerSingleton(candidateClass.getName(), DConfBuilder.getFromDb(candidateClass));
            //todo 这里可以在注解里面增加一个value
            defaultListableBeanFactory.registerSingleton(StringUtils.uncapitalize(candidateClass.getSimpleName()), DConfBuilder.getFromDb(candidateClass));
        }
    }

    /**
     * 扫描basePackages下的所有@DConf注解
     *
     * @param basePackages
     * @return
     */
    private Set<Class> scan(String... basePackages) {
        Set<Class> candidates = new LinkedHashSet<>();
        for (String basePackage : basePackages) {
            try {
                String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                        resolveBasePackage(basePackage) + '/' + this.resourcePattern;
                Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
                for (Resource resource : resources) {
                    if (resource.isReadable()) {
                        try {
                            //扫描包路径
                            MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
                            //判断是否有此注解
                            if (metadataReader.getAnnotationMetadata().getAnnotations().isPresent(DConf.class)) {
                                Class<?> aClass = Class.forName(metadataReader.getClassMetadata().getClassName());
                                candidates.add(aClass);
                            }
                        } catch (Throwable ex) {
                            log.error("Failed to read candidate component class: " + resource, ex);
                        }
                    }
                }
            } catch (IOException ex) {
                throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
            }
        }
        return candidates;
    }

    protected String resolveBasePackage(String basePackage) {
        return ClassUtils.convertClassNameToResourcePath(this.environment.resolveRequiredPlaceholders(basePackage));
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
    }

}
