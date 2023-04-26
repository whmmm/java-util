package org.whmmm.util.springtest.beanutil;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;

/**
 * <p><b> ----------------------- </b></p>
 * <p><b> author: whmmm           </b></p>
 * <p><b> date  : 2023/4/26 9:21 </b></p>
 *
 * @author whmmm
 */
class BeanUtilTest {
    @Test
    public void test() {
        ClassParent parent = new ClassParent();
        parent.setName("this is parent");

        ClassChildren children = new ClassChildren();
        BeanUtils.copyProperties(parent, children);

        System.out.println(children);
    }
}

@Data
class ClassParent {
    private String name;

    public String getName() {
        return name + " -- dfsdfsd";
    }
}

@Data
class ClassChildren {
    private String name;
}