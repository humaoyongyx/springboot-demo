package issac.study.mbp.core.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import issac.study.mbp.core.model.BaseModel;
import issac.study.mbp.core.service.BaseCrudService;

/**
 * @author issac.hu
 */
public class BaseCrudServiceImpl<M extends BaseMapper<T>, T extends BaseModel, V> extends GeneralCrudServiceImpl<M, T, V> implements BaseCrudService<T, V> {


    @Override
    protected T saveCustom(T model) {
        model.setCreatedBy(1);
        return model;
    }

    @Override
    protected T updateCustom(T model) {
        model.setUpdatedBy(1);
        return model;
    }


}
