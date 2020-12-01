package issac.study.cache.core.jpa;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author issac.hu
 */
public class JpaQueryRepository {

    private static JpaQueryTemplate jpaQueryTemplate;

    @Autowired
    public void setJpaQueryTemplate(JpaQueryTemplate jpaQueryTemplate) {
        JpaQueryRepository.jpaQueryTemplate = jpaQueryTemplate;
    }

    /**
     * 获取JpaQueryTemplate
     *
     * @return
     */
    public static JpaQueryTemplate getJpaQueryTemplate() {
        return jpaQueryTemplate;
    }

    /**
     * 快捷获取query的方式
     *
     * @param eClass
     * @param <E>
     * @return
     */
    public static <E> JpaQueryTemplate.Query<E> build(Class<E> eClass) {
        return jpaQueryTemplate.build(eClass);
    }

    /**
     * 获取列表
     *
     * @param consumer
     * @param eClass
     * @param <E>
     * @return
     */
    public static <E> List<E> getList(Consumer<JpaQueryTemplate.Query<E>> consumer, Class<E> eClass) {
        JpaQueryTemplate.Query<E> query = jpaQueryTemplate.build(eClass);
        consumer.accept(query);
        return query.query();
    }

    /**
     * 获取总数
     *
     * @param consumer
     * @param eClass
     * @param <E>
     * @return
     */
    public static <E> long getTotal(Consumer<JpaQueryTemplate.Query<E>> consumer, Class<E> eClass) {
        JpaQueryTemplate.Query<E> query = jpaQueryTemplate.build(eClass);
        consumer.accept(query);
        return query.count();
    }

    /**
     * 列表获取第一个
     *
     * @param consumer
     * @param eClass
     * @param <E>
     * @return
     */
    public static <E> Optional<E> getFirstOne(Consumer<JpaQueryTemplate.Query<E>> consumer, Class<E> eClass) {
        JpaQueryTemplate.Query<E> query = jpaQueryTemplate.build(eClass);
        consumer.accept(query);
        return query.queryFirstOne();
    }

    /**
     * 快捷方式
     *
     * @param consumer
     * @param eClass
     * @param vClass
     * @param <E>
     * @param <V>
     * @return
     */
    public static <E, V> Optional<V> getFirstOne(Consumer<JpaQueryTemplate.Query<E>> consumer, Class<E> eClass, Class<V> vClass) {
        JpaQueryTemplate.Query<E> query = jpaQueryTemplate.build(eClass);
        consumer.accept(query);
        return query.queryFirstOne(vClass);
    }

    /**
     * 获取唯一一个，多个报错
     *
     * @param consumer
     * @param eClass
     * @param <E>
     * @return
     */
    public static <E> E getSingleOne(Consumer<JpaQueryTemplate.Query<E>> consumer, Class<E> eClass) {
        JpaQueryTemplate.Query<E> query = jpaQueryTemplate.build(eClass);
        consumer.accept(query);
        return query.querySingleOne();
    }

}
