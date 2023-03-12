package org.whmmm.util.httpclient;


import org.slf4j.Logger;

import javax.annotation.Nullable;


/**
 * 声明
 * <p><b> ----------------------- </b></p>
 * <p><b> author: whmmm           </b></p>
 * <p><b> date  : 2023/3/9 16:54 </b></p>
 *
 * @author whmmm
 */
public interface IRequestExecutor {


    default boolean isDebug() {
        return true;
    }

    /**
     * 慢接口时间, 默认为 毫秒
     *
     * @return -
     */
    default long getSlowApiTime() {
        return 800;
    }

    @Nullable
    Object executeRequest(RequestMeta meta);

    @Nullable
    default Logger getLogger() {
        return null;
    }

}
