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
            baseTreeEntity.setChildNum(INIT_CHILD_NUM);
            baseTreeEntity.setChildSeq(INIT_CHILD_SEQ);
            BaseTreeEntity db = (BaseTreeEntity) this.baseJpaRepository().save(baseTreeEntity);
            db.setRootId(db.getRootId());
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
            baseTreeEntity.setChildNum(INIT_CHILD_NUM);
            baseTreeEntity.setChildSeq(INIT_CHILD_SEQ);
            baseTreeEntity.setRootId(parentDb.getRootId());
            if (parentDb.getLeaf()) {
                parentDb.setLeaf(false);
            }
            Integer currentChildSeq = parentDb.getChildSeq() + 1;
            Integer childNum = parentDb.getChildNum() + 1;
            parentDb.setChildSeq(currentChildSeq);
            parentDb.setChildNum(childNum);
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
            // 如果它是叶子节点
            if (dbForUpdate.getLeaf()) {

            }

            if (baseTreeReq.getDeeper() != null && baseTreeReq.getDeeper()) {

            } else {

            }
        }
        return null;
    }


}
