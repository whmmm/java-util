package org.whmmm.util.httpclient;


import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>{@code
 * 处理执行异常的情况,
 * 比如 504, 404 等等..
 * 还可以处理 gson 的解析异常
 * 比如
 *  com.google.gson.JsonParseException
 *  com.google.gson.JsonSyntaxException
 *
 * }</pre>
 *
 * <p><b> ----------------------- </b></p>
 * <p><b> author: whmmm           </b></p>
 * <p><b> date  : 2023/3/9 17:08 </b></p>
 *
 * @author whmmm
 */
public interface IExecutorExceptionHandler<T> {

    /**
     * 支持处理的异常
     * <pre>{@code
     * 默认实现的返回值为
     * com.google.gson.JsonParseException.class
     * com.google.gson.JsonSyntaxException.class
     * }</pre>
     *
     * @return -
     */
    @Nonnull
    default List<Class<? extends Throwable>> supportException() {
        ArrayList<Class<? extends Throwable>> list = new ArrayList<>(2);
        list.add(com.google.gson.JsonParseException.class);
        list.add(com.google.gson.JsonSyntaxException.class);
        return list;
    }

    /**
     * <pre>{@code
     * 处理执行异常的情况,
     * 比如 504, 404 等等..
     * 还可以处理 gson 的解析异常
     *  比如
     *   com.google.gson.JsonParseException
     *   com.google.gson.JsonSyntaxException
     * }</pre>
     *
     * @param e 异常信息
     * @return -
     * @throws Exception -
     */
    T handle(Exception e, RequestMeta meta) throws Exception;
}
