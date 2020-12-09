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
            this.baseJpaRepository().updateRootIdById(db.getId(), db.getId());
            result = db;
        } else {
            Optional parent = this.baseJpaRepository().findById(parentId);
            if (!parent.isPresent()) {
                BusinessRuntimeException.error("节点的父项id不存在");
            }
            BaseTreeEntity parentDb = (BaseTreeEntity) parent.get();
            baseTreeEntity.setDepth(parentDb.getDepth() + 1);
            baseTreeEntity.setParentId(parentId);
            baseTreeEntity.setIdPath(getChildIdPathPrefix(parentDb));
            baseTreeEntity.setChildSeq(INIT_CHILD_SEQ);
            baseTreeEntity.setRootId(parentDb.getRootId());
            baseTreeEntity.setLeaf(true);
            Integer currentChildSeq = parentDb.getChildSeq() + 1;
            baseTreeEntity.setSeq(currentChildSeq);
            this.baseJpaRepository().updateChildSeqAndLeafById(currentChildSeq, false, parentDb.getId());
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
        //清除此值，防止被copy到db对象中
        baseTreeReq.setParentId(null);
        if (parentId == null) {
            return super.update(baseReq, vClass, includeNullValue);
        } else {
            BaseTreeEntity dbForUpdate = checkReqForUpdate(baseReq, includeNullValue);
            //parentId 相同只需更改内容
            if (dbForUpdate.getParentId() == parentId.intValue()) {
                Object updateDb = this.baseJpaRepository().save(dbForUpdate);
                return ConvertUtils.convertObject(updateDb, vClass);
            }
            // 如果它是叶子节点，只需修改parent和它本身
            if (dbForUpdate.getLeaf()) {
                BaseTreeEntity newParent = addTo(parentId, dbForUpdate.getRootId());
                resetParentForDel(dbForUpdate);
                resetForUpdateParent(dbForUpdate, newParent);
                Object updateDb = this.baseJpaRepository().save(dbForUpdate);
                return ConvertUtils.convertObject(updateDb, vClass);
            }
            //非叶子节点修改父类
            if (baseTreeReq.getDeeper() != null && baseTreeReq.getDeeper()) {
                BaseTreeEntity newParent = addToWithLeaf(dbForUpdate, parentId);
                resetParentForDel(dbForUpdate, 1);
                String newIdPath = getChildIdPathPrefix(newParent);
                String currentIdPath = dbForUpdate.getIdPath();
                int depthIncr = newParent.getDepth() + 1 - dbForUpdate.getDepth();
                String childIdPath = dbForUpdate.getIdPath() + ID_PATH_DELIMITER + dbForUpdate.getId() + LIKE_SUFFIX;
                this.baseJpaRepository().updateTreeByIncr(depthIncr, currentIdPath, newIdPath, childIdPath);
                resetForUpdateParent(dbForUpdate, newParent);
                Object updateDb = this.baseJpaRepository().save(dbForUpdate);
                return ConvertUtils.convertObject(updateDb, vClass);
            } else {
                throw BusinessRuntimeException.error("此节点有子项，禁止修改父项");
            }
        }
    }

    private void resetForUpdateParent(BaseTreeEntity dbForUpdate, BaseTreeEntity newParent) {
        dbForUpdate.setIdPath(getChildIdPathPrefix(newParent));
        dbForUpdate.setDepth(newParent.getDepth() + 1);
        dbForUpdate.setParentId(newParent.getId());
        dbForUpdate.setSeq(newParent.getChildSeq() + 1);
    }

    private BaseTreeEntity addToWithLeaf(BaseTreeEntity db, Integer parentId) {
        Integer rootId = db.getRootId();
        BaseTreeEntity parent = checkParent(parentId, rootId);
        String childIdPath = getChildIdPathPrefix(db);
        if (parent.getIdPath().startsWith(childIdPath)) {
            throw BusinessRuntimeException.error("不能将节点追加到其子节点上");
        }
        parent = resetParentForAdd(parent);
        return parent;
    }

    private String getChildIdPathPrefix(BaseTreeEntity db) {
        return db.getIdPath() + ID_PATH_DELIMITER + db.getId();
    }

    private BaseTreeEntity addTo(Integer parentId, Integer rootId) {
        BaseTreeEntity parent = checkParent(parentId, rootId);
        parent = resetParentForAdd(parent);
        return parent;
    }

    private BaseTreeEntity resetParentForAdd(BaseTreeEntity parent) {
        int childSeq = parent.getChildSeq() + 1;
        parent.setChildSeq(childSeq);
        parent.setLeaf(false);
        this.baseJpaRepository().updateChildSeqAndLeafById(childSeq, false, parent.getId());
        return parent;
    }

    private BaseTreeEntity checkParent(Integer parentId, Integer rootId) {
        Optional parentOp = this.baseJpaRepository().findById(parentId);
        if (!parentOp.isPresent()) {
            throw BusinessRuntimeException.error("父节点不存在");
        }
        BaseTreeEntity parent = (BaseTreeEntity) parentOp.get();
        if (!rootId.equals(parent.getRootId())) {
            throw BusinessRuntimeException.error("不能跨树更新");
        }
        return parent;
    }


    @Override
    public Integer deleteById(Integer id) {
        Optional byId = this.baseJpaRepository().findById(id);
        if (byId.isPresent()) {
            BaseTreeEntity db = (BaseTreeEntity) byId.get();
            if (!db.getLeaf()) {
                throw BusinessRuntimeException.error("不能删除有子节点的数据");
            }
            super.deleteById(id);
            resetParentForDel(db);
            return id;
        }
        return null;
    }

    @Override
    public Integer deleteById(Integer id, boolean deeper) {
        if (deeper) {
            Optional byId = this.baseJpaRepository().findById(id);
            if (byId.isPresent()) {
                BaseTreeEntity db = (BaseTreeEntity) byId.get();
                super.deleteById(id);
                resetParentForDel(db);
                if (!db.getLeaf()) {
                    String nodeIdPath = db.getIdPath() + ID_PATH_DELIMITER + db.getId() + LIKE_SUFFIX;
                    this.baseJpaRepository().deleteByIdPathIsLike(nodeIdPath);
                }
                return id;
            }
            return null;
        } else {
            return deleteById(id);
        }
    }

    private void resetParentForDel(BaseTreeEntity db) {
        resetParentForDel(db, 0);
    }

    private void resetParentForDel(BaseTreeEntity db, int offset) {
        if (offset < 0) {
            offset = 0;
        }
        Integer parentId = db.getParentId();
        if (parentId != null) {
            int ct = this.baseJpaRepository().countByParentId(parentId);
            //如果已经没有子节点了，那么更新节点为叶子节点
            if (ct == offset) {
                this.baseJpaRepository().updateParentToLeaf(parentId);
            }
        }
    }
}
