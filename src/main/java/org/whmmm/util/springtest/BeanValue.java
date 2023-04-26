package org.whmmm.util.springtest;

import lombok.Getter;

/**
 * <p><b> ----------------------- </b></p>
 * <p><b> author: whmmm           </b></p>
 * <p><b> date  : 2023/4/25 17:26 </b></p>
 *
 * @author whmmm
 */
@SuppressWarnings("rawtypes")
@Getter
class BeanValue {
    private String name;
    private Class type;
    private Object value;

    public BeanValue(String name, Class type, Object value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }
}
