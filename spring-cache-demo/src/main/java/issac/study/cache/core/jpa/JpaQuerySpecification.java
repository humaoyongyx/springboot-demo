package issac.study.cache.core.jpa;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.function.Consumer;

/**
 * jpa查询的Specification 实现类
 *
 * @author humy6
 */
public interface JpaQuerySpecification<E> extends Specification<E>, Consumer<JpaQueryTemplate.Query> {

    /**
     * Restriction类的接口补充，可以获取Specification类
     *
     * @param root
     * @param criteriaQuery
     * @param criteriaBuilder
     * @return
     */
    @Override
    default Predicate toPredicate(Root<E> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        JpaQueryTemplate.Query<E> query = new JpaQueryTemplate().buildSpecification(root, criteriaQuery, criteriaBuilder);
        criteriaQuery.distinct(true);
        accept(query);
        return query.restrictions();
    }

    /**
     * JpaQuerySpecification 快捷方法
     *
     * @param req
     * @param eClass
     * @param <R>
     * @param <E>
     * @return
     */
    static <R, E> JpaQuerySpecification<E> and(R req, Class<E> eClass) {
        return (JpaQuerySpecification<E>) query -> {
            query.and(req, eClass);
        };
    }

}
