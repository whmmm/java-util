package org.whmmm.util.springtest;

import java.lang.annotation.*;

/**
 * <p><b> ----------------------- </b></p>
 * <p><b> author: whmmm           </b></p>
 * <p><b> date  : 2023/4/25 17:25 </b></p>
 *
 * @author whmmm
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@interface Inject {
}
