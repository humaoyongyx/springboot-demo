package issac.study.mbp.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import issac.study.mbp.core.exception.BusinessRuntimeException;
import issac.study.mbp.core.mapper.BaseTreeMapper;
import issac.study.mbp.core.model.BaseTreeModel;
import issac.study.mbp.core.req.BaseTreeReq;
import issac.study.mbp.core.service.BaseTreeCrudService;
import issac.study.mbp.core.utils.ConvertUtils;
import issac.study.mbp.core.vo.BaseTreeVo;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 树形结构的基础接口
 *
 * @author issac.hu
 */
public class BaseTreeCrudServiceImpl<M extends BaseTreeMapper<T>, T extends BaseTreeModel, V extends BaseTreeVo> extends BaseCrudServiceImpl<M, T, V> implements BaseTreeCrudService<T, V> {

    /**
     * 无需处理的值
     *
     * @param baseTreeReq
     */
    private void removeNotNeedValue(BaseTreeReq baseTreeReq) {
        baseTreeReq.setRootId(null);
    }

    @Override
    public V save(BaseTreeReq baseTreeReq) {
        removeNotNeedValue(baseTreeReq);
        T baseTreeEntity = ConvertUtils.convertObject(baseTreeReq, entityClass);
        Integer parentId = baseTreeEntity.getParentId();
        T result;
        if (parentId == null) {
            baseTreeEntity.setDepth(ROOT_DEPTH);
            baseTreeEntity.setParentId(null);
            baseTreeEntity.setIdPath("");
            baseTreeEntity.setLeaf(true);
            baseTreeEntity.setSeq(SEQ_FIRST_START);
            baseTreeEntity.setChildSeq(INIT_CHILD_SEQ);
            T db = saveModel(baseTreeEntity);
            db.setRootId(db.getId());
            this.getBaseMapper().updateRootIdById(currentTableName(), db.getId(), db.getId());
            result = db;
        } else {
            T parentDb = this.getBaseMapper().selectById(parentId);
            if (parentDb == null) {
                throw BusinessRuntimeException.error("节点的父项id不存在");
            }
            baseTreeEntity.setDepth(parentDb.getDepth() + 1);
            baseTreeEntity.setParentId(parentId);
            baseTreeEntity.setIdPath(getChildIdPathPrefix(parentDb));
            baseTreeEntity.setChildSeq(INIT_CHILD_SEQ);
            baseTreeEntity.setRootId(parentDb.getRootId());
            baseTreeEntity.setLeaf(true);
            Integer currentChildSeq = parentDb.getChildSeq() + 1;
            baseTreeEntity.setSeq(currentChildSeq);
            this.getBaseMapper().updateChildSeqAndLeafById(currentTableName(), currentChildSeq, LEAF_FALSE, parentDb.getId());
            result = saveModel(baseTreeEntity);
        }
        return ConvertUtils.convertObject(result, voClass);
    }


    @Override
    public V update(BaseTreeReq baseTreeReq) {
        return update(baseTreeReq, false);
    }

    @Override
    public V update(BaseTreeReq baseTreeReq, boolean includeNullValue) {
        removeNotNeedValue(baseTreeReq);
        Integer parentId = baseTreeReq.getParentId();
        //清除此值，防止被copy到db对象中
        baseTreeReq.setParentId(null);
        if (parentId == null) {
            return super.update(baseTreeReq, includeNullValue);
        } else {
            T dbForUpdate = checkReqForUpdate(baseTreeReq, includeNullValue);
            //parentId 相同只需更改内容
            if (dbForUpdate.getParentId() != null && (dbForUpdate.getParentId() == parentId.intValue())) {
                this.updateById(dbForUpdate);
                return ConvertUtils.convertObject(dbForUpdate, voClass);
            }
            // 如果它是叶子节点，只需修改parent和它本身
            if (dbForUpdate.getLeaf()) {
                T newParent = addTo(parentId, dbForUpdate.getRootId());
                resetParentForDel(dbForUpdate, 1);
                resetForUpdateParent(dbForUpdate, newParent);
                this.updateById(dbForUpdate);
                return ConvertUtils.convertObject(dbForUpdate, voClass);
            }
            //非叶子节点修改父类
            if (baseTreeReq.getDeeper() != null && baseTreeReq.getDeeper()) {
                T newParent = addToWithLeaf(dbForUpdate, parentId);
                resetParentForDel(dbForUpdate, 1);
                String newIdPath = getChildIdPathPrefix(newParent);
                String currentIdPath = dbForUpdate.getIdPath();
                int depthIncr = newParent.getDepth() + 1 - dbForUpdate.getDepth();
                String childIdPath = dbForUpdate.getIdPath() + ID_PATH_DELIMITER + dbForUpdate.getId() + LIKE_SUFFIX;
                this.getBaseMapper().updateTreeByIncr(currentTableName(), depthIncr, currentIdPath, newIdPath, childIdPath);
                resetForUpdateParent(dbForUpdate, newParent);
                this.updateById(dbForUpdate);
                return ConvertUtils.convertObject(dbForUpdate, voClass);
            } else {
                throw BusinessRuntimeException.error("此节点有子项，禁止修改父项");
            }
        }
    }

    private void resetForUpdateParent(T dbForUpdate, T newParent) {
        dbForUpdate.setIdPath(getChildIdPathPrefix(newParent));
        dbForUpdate.setDepth(newParent.getDepth() + 1);
        dbForUpdate.setParentId(newParent.getId());
        dbForUpdate.setSeq(newParent.getChildSeq() + 1);
    }

    private T addToWithLeaf(T db, Integer parentId) {
        Integer rootId = db.getRootId();
        T parent = checkParent(parentId, rootId);
        String childIdPath = getChildIdPathPrefix(db);
        if (parent.getIdPath().startsWith(childIdPath)) {
            throw BusinessRuntimeException.error("不能将节点追加到其子节点上");
        }
        parent = resetParentForAdd(parent);
        return parent;
    }

    private String getChildIdPathPrefix(T db) {
        return db.getIdPath() + ID_PATH_DELIMITER + db.getId();
    }

    private T addTo(Integer parentId, Integer rootId) {
        T parent = checkParent(parentId, rootId);
        parent = resetParentForAdd(parent);
        return parent;
    }

    private T resetParentForAdd(T parent) {
        int childSeq = parent.getChildSeq() + 1;
        parent.setChildSeq(childSeq);
        parent.setLeaf(false);
        this.getBaseMapper().updateChildSeqAndLeafById(currentTableName(), childSeq, LEAF_FALSE, parent.getId());
        return parent;
    }

    private T checkParent(Integer parentId, Integer rootId) {
        T parent = this.getBaseMapper().selectById(parentId);
        if (parent == null) {
            throw BusinessRuntimeException.error("父节点不存在");
        }
        if (!rootId.equals(parent.getRootId())) {
            throw BusinessRuntimeException.error("不能跨树更新");
        }
        return parent;
    }


    @Override
    public Integer deleteById(Integer id) {
        T db = this.getBaseMapper().selectById(id);
        if (db != null) {
            if (!db.getLeaf()) {
                throw BusinessRuntimeException.error("不能删除有子节点的数据");
            }
            this.getBaseMapper().deleteById(id);
            resetParentForDel(db);
            return id;
        }
        return null;
    }

    @Override
    public Integer deleteById(Integer id, boolean deeper) {
        if (deeper) {
            T db = this.getBaseMapper().selectById(id);
            if (db != null) {
                this.getBaseMapper().deleteById(id);
                resetParentForDel(db);
                if (!db.getLeaf()) {
                    String nodeIdPath = db.getIdPath() + ID_PATH_DELIMITER + db.getId() + LIKE_SUFFIX;
                    this.getBaseMapper().deleteByIdPathIsLike(currentTableName(), nodeIdPath);
                }
                return id;
            }
            return null;
        } else {
            return deleteById(id);
        }
    }

    private void resetParentForDel(T db) {
        resetParentForDel(db, 0);
    }

    private void resetParentForDel(T db, int offset) {
        if (offset < 0) {
            offset = 0;
        }
        Integer parentId = db.getParentId();
        if (parentId != null) {
            int ct = this.getBaseMapper().countByParentId(currentTableName(), parentId);
            //如果已经没有子节点了，那么更新节点为叶子节点
            if (ct == offset) {
                this.getBaseMapper().updateToLeaf(currentTableName(), parentId);
            }
        }
    }

    public List<T> getChildList(Integer parentId) {
        T parent = this.getBaseMapper().selectById(parentId);
        return getChildList(parent);
    }

    public List<T> getChildList(T parent) {
        if (parent != null && !parent.getLeaf()) {
            String childIdPath = getChildIdPathPrefix(parent);
            List<T> childList = this.getBaseMapper().selectList(new QueryWrapper<T>().likeRight("id_path", childIdPath));
            return childList;
        }
        return new ArrayList<>();
    }


    @Override
    public List<V> tree(BaseTreeReq baseTreeReq) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        handleQueryWrapper(baseTreeReq, queryWrapper, getEntityClass(), "rootId", "parentId");
        return tree(baseTreeReq, queryWrapper);
    }

    @Override
    public List<V> tree(BaseTreeReq baseTreeReq, QueryWrapper<T> queryWrapper) {
        Integer parentId = baseTreeReq.getParentId();
        queryWrapper.eq(baseTreeReq.getRootId() != null, "root_id", baseTreeReq.getRootId());
        List<T> childList = new ArrayList<>();
        if (parentId != null) {
            T parent = this.getBaseMapper().selectById(parentId);
            if (parent == null) {
                return new ArrayList<>();
            }
            //获取所有子层
            if (baseTreeReq.getDeeper() != null && baseTreeReq.getDeeper()) {
                childList = getChildList(parent);
                if (childList.isEmpty()) {
                    return new ArrayList<>();
                }
            }
            queryWrapper.eq("parent_id", parentId);
        }
        List<T> result;
        if (childList.isEmpty()) {
            result = this.getBaseMapper().selectList(queryWrapper);
        } else {
            result = childList;
        }
        if (result.isEmpty()) {
            return new ArrayList<>();
        }
        List<V> vResult = ConvertUtils.convertList(result, voClass);
        Map<Integer, List<V>> childMap = vResult.stream().filter(it -> it.getParentId() != null).collect(Collectors.groupingBy(it -> it.getParentId()));
        List<V> rootList = getRootList(baseTreeReq, vResult, childMap);
        if (rootList == null) {
            Collections.sort(vResult, Comparator.comparingInt(BaseTreeVo::getSeq));
            return vResult;
        }
        setChildItem(rootList, childMap);
        return rootList;
    }


    private List<V> getRootList(BaseTreeReq baseTreeReq, List<V> vResult, Map<Integer, List<V>> childMap) {
        Integer parentId = baseTreeReq.getParentId();
        if (parentId == null) {
            return vResult.stream().filter(it -> it.getParentId() == null).collect(Collectors.toList());
        } else {
            // deeper=true
            if (baseTreeReq.getDeeper() != null && baseTreeReq.getDeeper()) {
                return childMap.get(parentId);
            } else {
                //deeper=false parentId不为空的时候，此时说明有子层，只需要直接返回即可
                return null;
            }
        }
    }

    private void setChildItem(List<V> rootList, Map<Integer, List<V>> childMap) {
        for (V root : rootList) {
            if (!root.getLeaf()) {
                List<V> childItems = childMap.get(root.getId());
                //出现这种情况，要么就是数据是手动修改的，要么是带有查询条件，破坏了树形结构
                if (childItems == null) {
                    continue;
                }
                Collections.sort(childItems, Comparator.comparingInt(BaseTreeVo::getSeq));
                root.setChildren(childItems);
                setChildItem(childItems, childMap);
            }
        }
    }

}
