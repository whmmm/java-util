package org.whmmm.util.httpclient;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 执行反射代理方法
 * <p><b> ----------------------- </b></p>
 * <p><b> author: whmmm           </b></p>
 * <p><b> date  : 2023/3/13 9:16 </b></p>
 *
 * @author whmmm
 */
@Slf4j
final class RequestExecutorWrapper {

    private final RequestExecutorParser parser = new RequestExecutorParser();
    private static final String SEP = System.lineSeparator();

    /**
     * 执行反射代理方法 (jdk 动态代理)
     *
     * @param client {@link DeclareClient}
     * @param method 反射方法信息
     * @param args   反射的参数
     * @return -
     * @throws Exception -
     */
    public Object execReflectMethod(DeclareClient client,
                                    Method method,
                                    Object[] args) throws Exception {
        String methodString = method.toString();
        RequestMetaTemplate template = parser.getTemplate(methodString, client, method, args);

        RequestMeta meta = parser.parseTemplate(client, args, template);

        if (meta.isAsync()) {
            ThreadPoolTaskExecutor taskExecutor = client.getAsyncTaskExecutor();
            if (taskExecutor == null) {
                throw new RuntimeException("线程池为空, 异步执行失败");
            }

            return taskExecutor.submit(() -> execRequestWrapper(client, meta));
        } else {
            return execRequestWrapper(client, meta);
        }
    }

    @SuppressWarnings("rawtypes")
    private Object execRequestWrapper(DeclareClient client, RequestMeta meta) throws Exception {
        IRequestExecutor executor = client.getExecutor();

        Object result = null;
        StopWatch watch = new StopWatch();
        StringBuilder sb = new StringBuilder();
        boolean hasError = false;

        watch.start();

        /*if (executor.isDebug()) {
            watch = new StopWatch();
            watch.start();
        }*/

        try {
            this.validateUrl(client, meta);
            result = executor.executeRequest(meta);
        } catch (Exception e) {
            try {
                IExecutorExceptionHandler handler = client.getExceptionHandler();
                if (handler == null ||
                    !this.supportException(e, handler)) {

                    throw e;
                } else {
                    return handler.handle(e, meta);
                }
            } catch (Exception ex2) {
                hasError = true;
                sb.append(SEP);
                sb.append("## 请求异常: ");
                sb.append(ex2.getMessage());

                throw ex2;
            }
        } finally {
            watch.stop();
            long millis = watch.getTotalTimeMillis();
            long slowApiTime = executor.getSlowApiTime();
            if (millis >= slowApiTime ||
                hasError) {

                RequestLog reqLog = getRequestLog(meta, getBody(meta));
                sb.append(SEP)
                  .append(String.format("## 慢响应时间阈值 %s(ms), 实际耗时 : %s(毫秒), %s(秒) ",
                                        slowApiTime,
                                        millis,
                                        watch.getTotalTimeSeconds()
                  ))
                  .append(SEP);

                Logger logger = executor.getLogger();
                if (logger == null) {
                    logger = log;
                }
                logger.warn(reqLog.dumpToLogStr(sb));
            }
        }
        return result;
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    private boolean supportException(Exception e, IExecutorExceptionHandler handler) {
        List<Class<?>> list = handler.supportException();
        for (Class clz : list) {
            if (clz.isInstance(e)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据参数创建一个日志 log 对象
     *
     * @param meta   -
     * @param result -
     * @return -
     */
    private static RequestLog getRequestLog(RequestMeta meta, Object result) {
        RequestLog reqLog = new RequestLog();
        reqLog.setRequestId("---");
        reqLog.setUrl(meta.getUrl());
        reqLog.setType(meta.getHttpMethod().name());
        reqLog.setParam("");
        reqLog.setBody(meta.getBody());
        reqLog.setLogLimitUsable(true);
        reqLog.setResult(result + "");
        reqLog.setMaxBodyLen(400);

        return reqLog;
    }


    private static String getBody(RequestMeta meta) {
        ResponseEntity<String> entity = meta.getResponseEntity();
        if (entity == null) {
            return null;
        }
        return entity.getBody();
    }

    /**
     * 校验 url 是否合法
     *
     * @param client -
     * @param meta   -
     */
    private void validateUrl(DeclareClient client, RequestMeta meta) {
        String url = meta.getUrl();
        if (!url.contains("://")) {
            StringBuilder sb = new StringBuilder(client.getBaseUrl());
            sb.append(url);
            meta.setUrl(
                meta.generateUrl(sb, meta.getQueryMap())
            );
        }
    }
}
