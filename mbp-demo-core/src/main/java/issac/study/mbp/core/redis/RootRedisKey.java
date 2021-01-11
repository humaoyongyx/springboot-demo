package issac.study.mbp.core.redis;


import issac.study.mbp.core.constant.CoreConstant;

import java.util.ArrayList;

/**
 * @author issac.hu
 */
public class RootRedisKey implements RedisKey {

    protected StringBuffer name;

    protected ArrayList<String> moduleKeyList = new ArrayList<>();

    {
        addModuleKey(CoreConstant.REDIS_ROOT);
    }

    protected RootRedisKey() {
    }

    @Override
    public String getKey() {
        StringBuffer sb = getModulePath();
        StringBuffer name = new StringBuffer(this.name);
        if (name != null && name.length() > 0) {
            name.deleteCharAt(name.length() - 1);
            return sb.append(name).toString();
        }
        return sb.toString();
    }

    @Override
    public String getModuleKey() {
        return getModulePath().toString();
    }

    private StringBuffer getModulePath() {
        StringBuffer sb = new StringBuffer();
        for (String item : moduleKeyList) {
            sb.append(item).append(DELIMITER);
        }
        return sb;
    }

    protected void addModuleKey(String key) {
        moduleKeyList.add(key);
    }

    public RootRedisKey name(String... names) {
        this.name = new StringBuffer();
        for (String name : names) {
            this.name.append(name).append(DELIMITER);
        }
        return this;
    }

}
