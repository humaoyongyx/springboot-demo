package issac.study.cache.core.service;

import issac.study.cache.exception.BusinessRuntimeException;
import issac.study.cache.model.base.BaseTreeEntity;
import issac.study.cache.req.base.BaseReq;
import issac.study.cache.req.base.BaseTreeReq;
import issac.study.cache.utils.ConvertUtils;

import java.util.Optional;

/**
 * @author issac.hu
 */
public abstract class AbstractBaseTreeServiceImpl extends AbstractBaseCrudServiceImpl implements BaseTreeService {

    protected abstract Class<? extends BaseTreeEntity> entityClass();

    @Override
    public <V> V save(BaseReq baseReq, Class<V> vClass) {
        BaseTreeEntity baseTreeEntity = ConvertUtils.convertObject(baseReq, entityClass());
        Integer parentId = baseTreeEntity.getParentId();
        baseTreeEntity.setId(null);
        Object result;
        if (parentId == null) {
            baseTreeEntity.setDepth(ROOT_DEPTH);
            baseTreeEntity.setParentId(null);
            baseTreeEntity.setIdPath("");
            baseTreeEntity.setLeaf(true);
            baseTreeEntity.setSeq(SEQ_FIRST_START);
            baseTreeEntity.setChildSeq(INIT_CHILD_SEQ);
            BaseTreeEntity db = (BaseTreeEntity) this.baseJpaRepository().save(baseTreeEntity);
            db.setRootId(db.getId());
            result = this.baseJpaRepository().save(db);
        } else {
            Optional parent = this.baseJpaRepository().findById(parentId);
            if (!parent.isPresent()) {
                BusinessRuntimeException.error("节点的父项id不存在");
            }
            BaseTreeEntity parentDb = (BaseTreeEntity) parent.get();
            baseTreeEntity.setDepth(parentDb.getDepth() + 1);
            baseTreeEntity.setParentId(parentId);
            baseTreeEntity.setIdPath(parentDb.getIdPath() + ID_PATH_DELIMITER + parentDb.getId());
            baseTreeEntity.setChildSeq(INIT_CHILD_SEQ);
            baseTreeEntity.setRootId(parentDb.getRootId());
            baseTreeEntity.setLeaf(true);
            if (parentDb.getLeaf()) {
                parentDb.setLeaf(false);
            }
            Integer currentChildSeq = parentDb.getChildSeq() + 1;
            parentDb.setChildSeq(currentChildSeq);
            baseTreeEntity.setSeq(currentChildSeq);
            this.baseJpaRepository().save(parentDb);
            result = this.baseJpaRepository().save(baseTreeEntity);
        }
        return ConvertUtils.convertObject(result, vClass);
    }

    @Override
    public <V> V update(BaseReq baseReq, Class<V> vClass) {
        return update(baseReq, vClass, false);
    }

    @Override
    public <V> V update(BaseReq baseReq, Class<V> vClass, boolean includeNullValue) {
        BaseTreeReq baseTreeReq = (BaseTreeReq) baseReq;
        Integer parentId = baseTreeReq.getParentId();
        if (parentId == null) {
            return super.update(baseReq, vClass, includeNullValue);
        } else {

            BaseTreeEntity dbForUpdate = (BaseTreeEntity) checkReqForUpdate(baseReq, includeNullValue);
            //parentId 相同只需更改内容
            if (dbForUpdate.getParentId() == parentId.intValue()) {
                Object updateDb = this.baseJpaRepository().save(dbForUpdate);
                return ConvertUtils.convertObject(updateDb, vClass);
            }
            // 如果它是叶子节点，只需修改parent和它本身
            if (dbForUpdate.getLeaf()) {
                BaseTreeEntity newParent = addTo(parentId);
                dbForUpdate.setIdPath(newParent.getIdPath() + ID_PATH_DELIMITER + newParent.getId());
                dbForUpdate.setDepth(newParent.getDepth() + 1);
                dbForUpdate.setParentId(newParent.getId());
                Object updateDb = this.baseJpaRepository().save(dbForUpdate);
                return ConvertUtils.convertObject(updateDb, vClass);
            }
            //非叶子节点修改父类
            if (baseTreeReq.getDeeper() != null && baseTreeReq.getDeeper()) {
                throw BusinessRuntimeException.error("not implement!");
            } else {
                throw BusinessRuntimeException.error("此节点有子项，禁止修改父项");
            }
        }
    }

    private BaseTreeEntity addTo(Integer parentId) {
        Optional parentOp = this.baseJpaRepository().findById(parentId);
        if (!parentOp.isPresent()) {
            throw BusinessRuntimeException.error("父节点不存在");
        }
        BaseTreeEntity parent = (BaseTreeEntity) parentOp.get();
        int seq = parent.getSeq() + 1;
        parent.setSeq(seq);
        parent = (BaseTreeEntity) this.baseJpaRepository().save(parent);
        return parent;
    }


    @Override
    public Object deleteById(Object id) {
        Optional byId = this.baseJpaRepository().findById(id);
        if (byId.isPresent()) {
            BaseTreeEntity db = (BaseTreeEntity) byId.get();
            if (!db.getLeaf()) {
                throw BusinessRuntimeException.error("不能删除有子节点的数据");
            }
            super.deleteById(id);
            Integer parentId = db.getParentId();
            if (parentId != null) {
                int ct = this.baseJpaRepository().countByParentId(parentId);
                if (ct == 0) {
                    BaseTreeEntity parent = (BaseTreeEntity) this.baseJpaRepository().findById(parentId).get();
                    parent.setLeaf(true);
                    this.baseJpaRepository().save(parent);
                }
            }
            return id;
        }
        return null;
    }


}
