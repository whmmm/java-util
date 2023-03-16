package org.whmmm.util.httpclient;

/**
 * {@link DeclareClient} 拦截器
 * <p><b> ----------------------- </b></p>
 * <p><b> author: whmmm           </b></p>
 * <p><b> date  : 2023/3/14 11:18 </b></p>
 *
 * @author whmmm
 */
public interface IRequestExecutorInterceptor {
    /**
     * 请求执行器执行
     *
     * @param meta 请求元数据信息
     */
    default void beforeExecute(RequestMeta meta) {

    }

    /**
     * 异常时执行
     *
     * @param e    异常信息
     * @param meta 请求元数据信息
     */
    default void onError(Exception e, RequestMeta meta) {

    }

    /**
     * 请求执行完成执行
     *
     * @param meta   请求元数据信息
     * @param result 这个参数是 http 接口详情的结果
     */
    default void onComplete(RequestMeta meta, Object result) {

    }
}
