package issac.study.mbp.service.register;

import issac.study.mbp.core.module.AbstractModuleRegister;
import issac.study.mbp.req.InfoReq;
import issac.study.mbp.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author issac.hu
 */
@Service
public class SimpleRegister extends AbstractModuleRegister {


    @Autowired
    InfoService infoService;

    @Override
    protected void register() {
        register("info", infoService, InfoReq.class);
    }
}
