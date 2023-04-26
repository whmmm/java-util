package org.whmmm.util.springtest;

import lombok.Data;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p><b> ----------------------- </b></p>
 * <p><b> author: whmmm           </b></p>
 * <p><b> date  : 2023/4/25 17:25 </b></p>
 *
 * @author whmmm
 */
@Data
class BeanContext {
    public final Map<String, BeanValue> beanValueMap = new ConcurrentHashMap<>();

    public BeanContext registerValue(BeanValue value) {
        beanValueMap.put(value.getName(), value);
        return this;
    }

    @Nullable
    public BeanValue findByName(String name) {
        return beanValueMap.get(name);
    }
}
