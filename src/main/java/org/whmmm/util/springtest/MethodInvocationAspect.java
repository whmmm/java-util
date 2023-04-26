package org.whmmm.util.springtest;

import java.lang.reflect.Method;

/**
 * <p><b> ----------------------- </b></p>
 * <p><b> author: whmmm           </b></p>
 * <p><b> date  : 2023/4/25 17:18 </b></p>
 *
 * @author whmmm
 */
interface MethodInvocationAspect {

    void before(Method method, Object[] args);

    Object after(Object invokeObject,
                 Method method,
                 Object[] args);
}
