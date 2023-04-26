package org.whmmm.util.springtest;

import lombok.Getter;
import lombok.Setter;

/**
 * <p><b> ----------------------- </b></p>
 * <p><b> author: whmmm           </b></p>
 * <p><b> date  : 2023/4/25 17:05 </b></p>
 *
 * @author whmmm
 */
public class TestInterImpl implements TestInter {

    @Getter
    @Setter
    @Inject
    private String serviceName;

    @Override
    public String test(String echoStr) {
        return "test:  " + echoStr;
    }
}
