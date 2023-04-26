package org.whmmm.util.springtest;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * <p><b> ----------------------- </b></p>
 * <p><b> author: whmmm           </b></p>
 * <p><b> date  : 2023/4/25 17:05 </b></p>
 *
 * @author whmmm
 */
@Slf4j
public class BeanFactoryTest {
    @Test
    public void test() {
        BeanContext context = new BeanContext();

        context.registerValue(
            new BeanValue("serviceName", String.class, "测试服务")
        );

        BeanFactory factory = new BeanFactory(context);

        TestInter bean = factory.getBean(TestInter.class, TestInterImpl.class);


        String s = bean.test("测试");


        log.info(s);
    }
}
