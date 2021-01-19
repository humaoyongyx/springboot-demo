package issac.study.mbp.core.support;

import issac.study.mbp.core.annotation.DConf;
import issac.study.mbp.core.annotation.EnableDConf;
import issac.study.mbp.core.builder.DConfBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Configuration注解上如果有@import的注解或父类被@import的注解 见:{@link issac.study.mbp.core.annotation.EnableDConf}，会进入此类
 *
 * @author issac.hu
 */
@Slf4j
public class DynamicConfigRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware, ResourceLoaderAware {
    private Environment environment;
    private ResourceLoader resourceLoader;

    /**
     * 这个方法没被@import一次，就会被调用一次，也即被@EnableDConf注解的@configuration配置类，会被调用多次
     *
     * @param importingClassMetadata
     * @param registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //获取EnableDConf的属性
        Set<String> packages = getScanPackages(importingClassMetadata);
        //扫描@DConf注解
        Set<Class> candidateClasses = scan(packages.toArray(new String[0]));
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) registry;
        for (Class candidateClass : candidateClasses) {
            // singletonBeanRegistry.registerSingleton(candidateClass.getName(), DConfBuilder.getFromDb(candidateClass));
            //todo 这里可以在注解里面增加一个value
            defaultListableBeanFactory.registerSingleton(StringUtils.uncapitalize(candidateClass.getSimpleName()), DConfBuilder.getFromDb(candidateClass));
        }
    }

    private Set<String> getScanPackages(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(EnableDConf.class.getName());
        AnnotationAttributes attributes = new AnnotationAttributes(annotationAttributes);
        String[] value = attributes.getStringArray("value");
        String[] basePackages = attributes.getStringArray("basePackages");
        Set<String> packages = new HashSet<>();
        if (value.length == 0 && basePackages.length == 0) {
            String className = importingClassMetadata.getClassName();
            packages.add(ClassUtils.getPackageName(className));
            return packages;
        }
        packages.addAll(Arrays.asList(value));
        packages.addAll(Arrays.asList(basePackages));
        return packages;
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
            ClassPathScanningCandidateComponentProvider scan = new ClassPathScanningCandidateComponentProvider(false, this.environment);
            scan.setResourceLoader(this.resourceLoader);
            scan.addIncludeFilter(new AnnotationTypeFilter(DConf.class));
            Set<BeanDefinition> candidateComponents = scan.findCandidateComponents(basePackage);
            for (BeanDefinition candidateComponent : candidateComponents) {

                try {
                    Class<?> aClass = Class.forName(candidateComponent.getBeanClassName());
                    candidates.add(aClass);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
//            try {
//                String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
//                        resolveBasePackage(basePackage) + '/' + this.resourcePattern;
//                Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
//                for (Resource resource : resources) {
//                    if (resource.isReadable()) {
//                        try {
//                            //扫描包路径
//                            MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
//                            //判断是否有此注解
//                            if (metadataReader.getAnnotationMetadata().getAnnotations().isPresent(DConf.class)) {
//                                Class<?> aClass = Class.forName(metadataReader.getClassMetadata().getClassName());
//                                candidates.add(aClass);
//                            }
//                        } catch (Throwable ex) {
//                            log.error("Failed to read candidate component class: " + resource, ex);
//                        }
//                    }
//                }
//            } catch (IOException ex) {
//                throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
//            }
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
        this.resourceLoader = resourceLoader;
    }

}
