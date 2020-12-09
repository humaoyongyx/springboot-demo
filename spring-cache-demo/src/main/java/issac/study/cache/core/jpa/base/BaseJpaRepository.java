package issac.study.cache.core.jpa.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@NoRepositoryBean
public interface BaseJpaRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
    /**
     * list参数
     *
     * @param ids
     * @return
     */
    List<T> findByIdIn(Collection<ID> ids);

    @Modifying
    @Transactional
    @Query(value = "delete from #{#entityName} where id = ?1")
    void deleteById(ID id);
}
