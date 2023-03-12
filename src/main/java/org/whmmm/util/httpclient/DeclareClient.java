package org.whmmm.util.httpclient;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.whmmm.util.jsr.Nullable;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * <p><b> ----------------------- </b></p>
 * <p><b> author: whmmm           </b></p>
 * <p><b> date  : 2023/3/9 8:53 </b></p>
 *
 * @author whmmm
 */
@Slf4j
public class DeclareClient implements Serializable, InvocationHandler {

    @Tolerate
    public DeclareClient() {
    }

    @Setter
    @Getter
    private String baseUrl;

    /**
     * 反射执行代码默认实现
     */
    @Getter
    private IRequestExecutor executor = DeclareClientFactory.REFLECT_EXECUTOR;

    private final RequestExecutorParser parser = new RequestExecutorParser();
    @Getter
    private IExecutorExceptionHandler exceptionHandler;

    /**
     * 异步线程池
     */
    @Nullable
    @Getter
    private ThreadPoolTaskExecutor asyncTaskExecutor;


    /**
     * <h3>jdk 动态代理</h3>
     * {@inheritDoc}<br/>
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //如果传进来是一个已实现的具体类（本次演示略过此逻辑)
        if (Object.class.equals(method.getDeclaringClass())) {
            try {
                return method.invoke(this, args);
            } catch (Throwable t) {
                log.error(t.getMessage(), t);
            }
        } else {
            //如果传进来的是一个接口（核心)
            return parser.execReflectMethod(this, method, args);
        }
        return null;
    }


    public static DeclareClientBuilder builder() {
        return new DeclareClientBuilder();
    }

    /**
     * see {@link lombok.Lombok}
     */
    public static class DeclareClientBuilder {
        // 反射执行代码默认实现
        private IRequestExecutor executor = DeclareClientFactory.REFLECT_EXECUTOR;
        // 异步线程池
        private ThreadPoolTaskExecutor asyncTaskExecutor;
        private IExecutorExceptionHandler exceptionHandler = null;

        /**
         * http client 执行实现
         *
         * @param executor -
         * @return -
         */
        public DeclareClientBuilder executor(RequestExecutor executor) {
            this.executor = executor;
            return this;
        }

        /**
         * 添加异常处理
         *
         * @param handler -
         * @return -
         */
        @SuppressWarnings("rawtypes")
        public DeclareClientBuilder exception(IExecutorExceptionHandler handler) {
            this.exceptionHandler = handler;
            return this;
        }

        /**
         * 异步执行所需的参数
         *
         * @param asyncTaskExecutor -
         * @return -
         */
        public DeclareClientBuilder asyncTaskExecutor(ThreadPoolTaskExecutor asyncTaskExecutor) {
            this.asyncTaskExecutor = asyncTaskExecutor;
            return this;
        }


        @SuppressWarnings("unchecked")
        public <T> T target(Class<T> cls, String baseUrl) {

            DeclareClient client = new DeclareClient();

            client.setBaseUrl(baseUrl);
            client.asyncTaskExecutor = this.asyncTaskExecutor;
            client.executor = this.executor;
            client.exceptionHandler = this.exceptionHandler;

            Object newProxyInstance = Proxy.newProxyInstance(
                cls.getClassLoader(),
                new Class[]{cls},
                client);
            return (T) newProxyInstance;
        }
    }
}
