//package issac.study.mbp.core.service.tree2;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import issac.study.mbp.core.exception.BusinessRuntimeException;
//import issac.study.mbp.core.mapper.tree2.BaseTreeMapper;
//import issac.study.mbp.core.model.tree2.BaseTreeModel;
//import issac.study.mbp.core.req.tree2.BaseTreeReq;
//import issac.study.mbp.core.service.impl.GeneralCrudServiceImpl;
//import issac.study.mbp.core.utils.tree2.ConvertUtils;
//import issac.study.mbp.core.vo.BaseTreeResp;
//
//
//import java.util.*;
//import java.util.function.BiFunction;
//import java.util.function.Consumer;
//import java.util.stream.Collectors;
//
///**
// * 树形结构的基础接口
// *
// * @author issac.hu
// */
//public class BaseTreeCrudServiceImpl<M extends BaseTreeMapper<T>, T extends BaseTreeModel, V extends BaseTreeResp> extends GeneralCrudServiceImpl<M, T, V> implements BaseTreeCrudService<T, V> {
//
//    /**
//     * 常量null
//     */
//    private final Consumer<V> nullTreeConsumer = v -> {
//    };
//
//    /**
//     * 无需处理的值
//     *
//     * @param baseTreeReq
//     */
//    private void removeNotNeedValue(BaseTreeReq baseTreeReq) {
//        baseTreeReq.setRootId(null);
//    }
//
//    @Override
//    public V saveTree(BaseTreeReq baseTreeReq) {
//        T result = saveTree0(baseTreeReq);
//        return ConvertUtils.toBean(result, voClass);
//    }
//
//    protected T saveTree0(BaseTreeReq baseTreeReq) {
//        Long id = baseTreeReq.getId();
//        Objects.requireNonNull(id, "id不能为空");
//        removeNotNeedValue(baseTreeReq);
//        T baseTreeEntity = ConvertUtils.toBean(baseTreeReq, entityClass);
//        Long parentId = baseTreeEntity.getParentId();
//        T result;
//        if (parentId == null || parentId == ROOT_PARENT_ID) {
//            baseTreeEntity.setDepth(ROOT_DEPTH);
//            baseTreeEntity.setParentId(ROOT_PARENT_ID);
//            baseTreeEntity.setIdPath(String.valueOf(id));
//            baseTreeEntity.setLeaf(LEAF_TRUE);
//            int sort = getRootSort(baseTreeReq);
//            baseTreeEntity.setSort(sort);
//            baseTreeEntity.setChildSort(INIT_CHILD_SORT);
//            baseTreeEntity.setRootId(id);
//            T db = saveModel(baseTreeEntity);
//            result = db;
//        } else {
//            T parentDb = this.getBaseMapper().selectById(parentId);
//            if (parentDb == null) {
//                throw BusinessRuntimeException.error("节点的父项id不存在");
//            }
//            baseTreeEntity.setDepth(parentDb.getDepth() + 1);
//            baseTreeEntity.setParentId(parentId);
//            baseTreeEntity.setIdPath(getIdPath(parentDb, id));
//            baseTreeEntity.setChildSort(INIT_CHILD_SORT);
//            baseTreeEntity.setRootId(parentDb.getRootId());
//            baseTreeEntity.setLeaf(LEAF_TRUE);
//            Integer sort = getNonRootSort(baseTreeReq, parentDb);
//            baseTreeEntity.setSort(sort);
//            result = saveModel(baseTreeEntity);
//        }
//        return result;
//    }
//
//    private int getRootSort(BaseTreeReq baseTreeReq) {
//        int sort;
//        Long nextId = baseTreeReq.getNextId();
//        if (nextId == null) {
//            Integer sortMax = this.getBaseMapper().maxSortByParentId(currentTableName(), ROOT_PARENT_ID);
//            if (sortMax == null || sortMax <= 0) {
//                sort = SORT_FIRST_START;
//            } else {
//                sort = sortMax + 1;
//            }
//        } else {
//            T nextNode = getBaseMapper().selectById(nextId);
//            if (nextNode == null) {
//                throw BusinessRuntimeException.error("下个节点不存在");
//            }
//            Integer nextSort = nextNode.getSort();
//            sort = nextSort;
//            this.getBaseMapper().incrNextSort(currentTableName(), nextSort, ROOT_PARENT_ID);
//        }
//        return sort;
//    }
//
//    private int getNonRootSort(BaseTreeReq baseTreeReq, T parentDb) {
//        int sort;
//        Long nextId = baseTreeReq.getNextId();
//        if (nextId == null) {
//            sort = parentDb.getChildSort() + 1;
//        } else {
//            T nextNode = getBaseMapper().selectById(nextId);
//            if (nextNode == null) {
//                throw BusinessRuntimeException.error("下个节点不存在");
//            }
//            Integer nextSort = nextNode.getSort();
//            sort = nextSort;
//            this.getBaseMapper().incrNextSort(currentTableName(), nextSort, parentDb.getId());
//        }
//        this.getBaseMapper().updateChildSortAndLeafById(currentTableName(), 1, LEAF_FALSE, parentDb.getId());
//        return sort;
//    }
//
//    @Override
//    public V updateTree(BaseTreeReq baseTreeReq) {
//        return updateTree(baseTreeReq, false);
//    }
//
//    @Override
//    public V updateTree(BaseTreeReq baseTreeReq, boolean includeEmptyValue) {
//        removeNotNeedValue(baseTreeReq);
//        Long parentId = baseTreeReq.getParentId();
//        //清除此值，防止被copy到db对象中
//        baseTreeReq.setParentId(null);
//        if (parentId == null) {
//            return super.update(baseTreeReq, includeEmptyValue);
//        } else {
//            T dbForUpdate = checkReqForUpdate(baseTreeReq, includeEmptyValue);
//            //将上面的parentId设置回来
//            baseTreeReq.setParentId(parentId);
//            moveNode(baseTreeReq, dbForUpdate);
//            return ConvertUtils.toBean(dbForUpdate, voClass);
//        }
//    }
//
//    @Override
//    public V updateNoTree(BaseTreeReq baseTreeReq) {
//        return update(baseTreeReq, false);
//    }
//
//    @Override
//    public V updateNoTree(BaseTreeReq baseTreeReq, boolean includeEmptyValue) {
//        ClearBaseTreeField(baseTreeReq);
//        return super.update(baseTreeReq, includeEmptyValue);
//    }
//
//
//    /**
//     * 更新数据，且移动节点
//     *
//     * @param baseTreeReq
//     * @param node
//     * @return
//     */
//    protected T moveNode(BaseTreeReq baseTreeReq, T node) {
//        Long parentId = baseTreeReq.getParentId();
//        //parentId 相同只需更改内容
//        if (node.getParentId() != null && (node.getParentId().equals(parentId))) {
//            T parent = checkParent(parentId, node.getRootId());
//            int sort = getNonRootSort(baseTreeReq, parent);
//            node.setSort(sort);
//        }
//        // 如果它是叶子节点，只需修改parent和它本身
//        else if (LEAF_TRUE.equals(node.getLeaf())) {
//            T newParent = addTo(parentId, node.getRootId());
//            resetParentForDel(node, 1);
//            resetForUpdateParent(baseTreeReq, node, newParent);
//        }
//        //非叶子节点修改父类
//        else if (baseTreeReq.getDeeper() != null && baseTreeReq.getDeeper()) {
//            T newParent = addToWithLeaf(node, parentId);
//            resetParentForDel(node, 1);
//            resetChild(node, newParent);
//            resetForUpdateParent(baseTreeReq, node, newParent);
//            //此时更新的是有叶子节点且非同节点更新且不是同一个parent节点
//        } else {
//            throw BusinessRuntimeException.error("此节点有子项，禁止修改父项");
//        }
//        this.updateById(node);
//        return node;
//    }
//
//    private void resetChild(T dbForUpdate, T newParent) {
//        String newIdPath = getIdPath(newParent, dbForUpdate.getId());
//        String currentIdPath = dbForUpdate.getIdPath();
//        int depthIncr = newParent.getDepth() + 1 - dbForUpdate.getDepth();
//        String childIdPathPrefixLike = getChildIdPathPrefixLike(dbForUpdate);
//        this.getBaseMapper().updateTreeByIncr(currentTableName(), depthIncr, currentIdPath, newIdPath, childIdPathPrefixLike);
//    }
//
//    private void resetForUpdateParent(BaseTreeReq baseTreeReq, T dbForUpdate, T newParent) {
//        dbForUpdate.setIdPath(getIdPath(newParent, dbForUpdate.getId()));
//        dbForUpdate.setDepth(newParent.getDepth() + 1);
//        dbForUpdate.setParentId(newParent.getId());
//        dbForUpdate.setSort(getNonRootSort(baseTreeReq, newParent));
//    }
//
//    private T addToWithLeaf(T db, Long parentId) {
//        Long rootId = db.getRootId();
//        T parent = checkParent(parentId, rootId);
//        String childIdPath = getChildIdPathPrefix(db);
//        if (parent.getIdPath().startsWith(childIdPath)) {
//            throw BusinessRuntimeException.error("不能将节点追加到其子节点上");
//        }
//        return parent;
//    }
//
//    /**
//     * 获取节点的IdPath
//     *
//     * @param parent 当前节点的父类
//     * @param nodeId 当前节点id
//     * @return
//     */
//    private String getIdPath(T parent, Long nodeId) {
//        return parent.getIdPath() + ID_PATH_DELIMITER + nodeId;
//    }
//
//    /**
//     * 获取节点子孙的IdPath的前缀
//     *
//     * @param node
//     * @return
//     */
//    private String getChildIdPathPrefix(T node) {
//        return node.getIdPath() + ID_PATH_DELIMITER;
//    }
//
//    private String getChildIdPathPrefixLike(T node) {
//        return node.getIdPath() + ID_PATH_DELIMITER + LIKE_SUFFIX;
//    }
//
//
//    private T addTo(Long parentId, Long rootId) {
//        T parent = checkParent(parentId, rootId);
//        return parent;
//    }
//
//    private T checkParent(Long parentId, Long rootId) {
//        T parent = this.getBaseMapper().selectById(parentId);
//        if (parent == null) {
//            throw BusinessRuntimeException.error("父节点不存在");
//        }
//        if (!rootId.equals(parent.getRootId())) {
//            throw BusinessRuntimeException.error("不能跨树更新");
//        }
//        return parent;
//    }
//
//    @Override
//    public Long deleteById(Long id) {
//        T db = this.getBaseMapper().selectById(id);
//        if (db != null) {
//            if (!LEAF_TRUE.equals(db.getLeaf())) {
//                throw BusinessRuntimeException.error("不能删除有子节点的数据");
//            }
//            super.deleteById(id);
//            resetParentForDel(db);
//            return id;
//        }
//        return null;
//    }
//
//    @Override
//    public Long deleteById(Long id, boolean deeper) {
//        if (deeper) {
//            T db = this.getBaseMapper().selectById(id);
//            if (db != null) {
//                super.deleteById(id);
//                resetParentForDel(db);
//                if (LEAF_TRUE.equals(db.getLeaf())) {
//                    String childIdPathPrefixLike = getChildIdPathPrefixLike(db);
//                    this.getBaseMapper().deleteByIdPathIsLike(currentTableName(), childIdPathPrefixLike);
//                }
//                return id;
//            }
//            return null;
//        } else {
//            return deleteById(id);
//        }
//    }
//
//    private void resetParentForDel(T db) {
//        resetParentForDel(db, 0);
//    }
//
//    private void resetParentForDel(T db, int offset) {
//        if (offset < 0) {
//            offset = 0;
//        }
//        Long parentId = db.getParentId();
//        if (parentId != ROOT_PARENT_ID) {
//            int ct = this.getBaseMapper().countByParentId(currentTableName(), parentId);
//            //如果已经没有子节点了，那么更新节点为叶子节点
//            if (ct == offset) {
//                this.getBaseMapper().updateToLeaf(currentTableName(), parentId);
//            }
//        }
//    }
//
//    protected List<T> getAllChildList(Long parentId) {
//        T parent = this.getBaseMapper().selectById(parentId);
//        return getAllChildList(parent);
//    }
//
//    protected List<T> getAllChildList(T parent) {
//        return getChildList(parent, true);
//    }
//
//    protected List<T> getChildList(T parent, boolean deeper) {
//        return getChildList(parent, deeper, null);
//    }
//
//    protected List<T> getChildList(T parent, boolean deeper, QueryWrapper<T> queryWrapper) {
//        List<T> childList = new ArrayList<>();
//        if (parent == null) {
//            return childList;
//        }
//        if (queryWrapper == null) {
//            queryWrapper = new QueryWrapper();
//        }
//        if (ROOT_PARENT_ID.equals(parent.getId())) {
//            if (deeper) {
//                childList = this.getBaseMapper().selectList(queryWrapper);
//            } else {
//                childList = this.getBaseMapper().selectList(queryWrapper.eq("parent_id", ROOT_PARENT_ID));
//            }
//        } else {
//            if (!isLeaf(parent)) {
//                if (deeper) {
//                    String childIdPath = getChildIdPathPrefix(parent);
//                    childList = this.getBaseMapper().selectList(queryWrapper.likeRight("id_path", childIdPath));
//                } else {
//                    childList = this.getBaseMapper().selectList(queryWrapper.eq("parent_id", parent.getId()));
//                }
//            }
//        }
//        return childList;
//    }
//
//
//    protected boolean isLeaf(T node) {
//        if (LEAF_TRUE.equals(node.getLeaf())) {
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public void copyTreeUp(BaseTreeReq req, Long nextId) {
//        T nextNode = this.getBaseMapper().selectById(nextId);
//        if (nextNode == null) {
//            throw BusinessRuntimeException.error("不存在节点id");
//        }
//        copyTreeUp(req, nextNode, null);
//    }
//
//    @Override
//    public void copyTreeUp(BaseTreeReq req, T nextNode, List<T> nextNodeChildrenList) {
//        copyTreeUp(req, nextNode, nextNodeChildrenList, null);
//    }
//
//    public void copyTreeUp(BaseTreeReq req, T nextNode, List<T> nextNodeChildrenList, BiFunction<T, T, T> oldAndCopyNodeFunc) {
//        long newId = IdGenerator.nextId();
//        req.setId(newId);
//        req.setParentId(nextNode.getParentId());
//        req.setNextId(nextNode.getId());
//        T newParent = saveTree0(req);
//        if (oldAndCopyNodeFunc != null) {
//            oldAndCopyNodeFunc.apply(nextNode, newParent);
//        }
//        if (!isLeaf(nextNode)) {
//            this.getBaseMapper().updateToNonLeaf(currentTableName(), newParent.getId());
//            if (nextNodeChildrenList == null) {
//                nextNodeChildrenList = getAllChildList(nextNode);
//            }
//            Map<Long, List<T>> nextNodeParentMap = nextNodeChildrenList.stream().collect(Collectors.groupingBy(it -> it.getParentId()));
//            List<T> newChildSaveList = new ArrayList<>();
//            copyAndResetNewChildList(nextNode, newParent, nextNodeParentMap, newChildSaveList, oldAndCopyNodeFunc);
//            saveModelAll(newChildSaveList);
//        }
//    }
//
//    private void copyAndResetNewChildList(T oldNodeParent, T newNodeParent, Map<Long, List<T>> nextNodeParentMap, List<T> newChildSaveList, BiFunction<T, T, T> oldAndCopyNodeFunc) {
//        List<T> oldChildList = nextNodeParentMap.get(oldNodeParent.getId());
//        for (T oldNode : oldChildList) {
//            T newNode = ConvertUtils.toBean(oldNode, entityClass);
//            newNode.setId(IdGenerator.nextId());
//            newNode.setParentId(newNodeParent.getId());
//            newNode.setIdPath(getIdPath(newNodeParent, newNode.getId()));
//            newNode.setRootId(newNodeParent.getRootId());
//            if (oldAndCopyNodeFunc != null) {
//                newNode = oldAndCopyNodeFunc.apply(oldNode, newNode);
//            }
//            newChildSaveList.add(newNode);
//            if (!isLeaf(oldNode)) {
//                copyAndResetNewChildList(oldNode, newNode, nextNodeParentMap, newChildSaveList, oldAndCopyNodeFunc);
//            }
//        }
//    }
//
//    protected boolean isRoot(T node) {
//        boolean isRoot = false;
//        if (node.getId().equals(node.getRootId())) {
//            isRoot = true;
//        }
//        return isRoot;
//    }
//
//    protected String getParentIdPathPrefix(T node) {
//        return node.getIdPath().replace(String.valueOf(node.getIdPath()), "");
//    }
//
//    @Override
//    public List<V> tree(BaseTreeReq baseTreeReq) {
//        return tree(baseTreeReq, nullTreeConsumer);
//    }
//
//    @Override
//    public List<V> tree(BaseTreeReq baseTreeReq, Consumer<V> nodeConsumer) {
//        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
//        handleQueryWrapper(baseTreeReq, queryWrapper, getEntityClass(), "rootId", "parentId");
//        return tree(baseTreeReq, queryWrapper, nodeConsumer);
//    }
//
//    @Override
//    public List<V> tree(BaseTreeReq baseTreeReq, QueryWrapper<T> queryWrapper) {
//        return tree(baseTreeReq, queryWrapper, null);
//    }
//
//    @Override
//    public List<V> tree(BaseTreeReq baseTreeReq, QueryWrapper<T> queryWrapper, Consumer<V> nodeConsumer) {
//        Long parentId = baseTreeReq.getParentId();
//        queryWrapper.eq(baseTreeReq.getRootId() != null, "root_id", baseTreeReq.getRootId());
//        List<T> childList = new ArrayList<>();
//        if (parentId != null) {
//            if (parentId != ROOT_PARENT_ID) {
//                T parent = this.getBaseMapper().selectById(parentId);
//                if (parent == null) {
//                    return new ArrayList<>();
//                }
//                //获取所有子层
//                if (baseTreeReq.getDeeper() != null && baseTreeReq.getDeeper()) {
//                    childList = getChildList(parent, true, queryWrapper);
//                    if (childList.isEmpty()) {
//                        return new ArrayList<>();
//                    }
//                }
//            } else {
//                if (baseTreeReq.getDeeper() != null && baseTreeReq.getDeeper()) {
//                    T root = BeanUtils.clone(new BaseTreeReq(), entityClass);
//                    root.setId(ROOT_PARENT_ID);
//                    childList = getChildList(root, true, queryWrapper);
//                    if (childList.isEmpty()) {
//                        return new ArrayList<>();
//                    }
//                }
//            }
//            queryWrapper.eq("parent_id", parentId);
//        }
//        List<T> result;
//        if (childList.isEmpty()) {
//            result = this.getBaseMapper().selectList(queryWrapper);
//        } else {
//            result = childList;
//        }
//        if (result.isEmpty()) {
//            return new ArrayList<>();
//        }
//        List<V> vResult = ConvertUtils.toList(result, voClass);
//        if (nodeConsumer != null && nodeConsumer != nullTreeConsumer) {
//            for (V v : vResult) {
//                nodeConsumer.accept(v);
//            }
//        }
//        Map<Long, List<V>> childMap = vResult.stream().filter(it -> it.getParentId() != ROOT_PARENT_ID).collect(Collectors.groupingBy(it -> it.getParentId()));
//        List<V> rootList = getRootList(baseTreeReq, vResult, childMap);
//        if (rootList == null) {
//            Collections.sort(vResult, Comparator.comparingInt(BaseTreeResp::getSort));
//            return vResult;
//        }
//        Collections.sort(rootList, Comparator.comparingInt(BaseTreeResp::getSort));
//        setChildItem(rootList, childMap);
//        return rootList;
//    }
//
//    private List<V> getRootList(BaseTreeReq baseTreeReq, List<V> vResult, Map<Long, List<V>> childMap) {
//        Long parentId = baseTreeReq.getParentId();
//        if (parentId == null || parentId == ROOT_PARENT_ID) {
//            return vResult.stream().filter(it -> it.getParentId() == ROOT_PARENT_ID).collect(Collectors.toList());
//        } else {
//            // deeper=true
//            if (baseTreeReq.getDeeper() != null && baseTreeReq.getDeeper()) {
//                return childMap.get(parentId);
//            } else {
//                //deeper=false parentId不为空的时候，此时说明有子层，只需要直接返回即可
//                return null;
//            }
//        }
//    }
//
//    private void setChildItem(List<V> rootList, Map<Long, List<V>> childMap) {
//        for (V root : rootList) {
//            if (!LEAF_TRUE.equals(root.getLeaf())) {
//                List<V> childItems = childMap.get(root.getId());
//                //出现这种情况，要么就是数据是手动修改的，要么是带有查询条件，破坏了树形结构
//                if (childItems == null) {
//                    continue;
//                }
//                Collections.sort(childItems, Comparator.comparingInt(BaseTreeResp::getSort));
//                root.setChildren(childItems);
//                setChildItem(childItems, childMap);
//            }
//        }
//    }
//
//    /**
//     * 获取第一个子节点
//     *
//     * @param parentId
//     * @return
//     */
//    protected T getFirstChildByParentId(Long parentId) {
//        Map<String, Object> result = this.getBaseMapper().getFirstChildByParentId(currentTableName(), parentId);
//        return ConvertUtils.toBean(result, entityClass);
//    }
//
//    private void ClearBaseTreeField(BaseTreeReq baseTreeReq) {
//        baseTreeReq.setParentId(null);
//        baseTreeReq.setNextId(null);
//        baseTreeReq.setRootId(null);
//    }
//}
