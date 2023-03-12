package org.whmmm.util.httpclient;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于解析注解和元数据. <br/>
 * 处理结果是否异步等
 * <p><b> ----------------------- </b></p>
 * <p><b> author: whmmm           </b></p>
 * <p><b> date  : 2023/3/9 12:45 </b></p>
 *
 * @author whmmm
 */
@Slf4j
class RequestExecutorParser {
    private static final Map<String, RequestMetaTemplate> TEMPLATE_CACHE = new ConcurrentHashMap<>();
    private static final String SEP = System.lineSeparator();


    public Object execReflectMethod(DeclareClient client, Method method, Object[] args) throws Exception {
        String methodString = method.toString();
        RequestMetaTemplate template = this.getTemplate(methodString, client, method, args);

        RequestMeta meta = parseTemplate(client, args, template);

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
    public Object execRequestWrapper(DeclareClient client, RequestMeta meta) throws Exception {
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
    protected boolean supportException(Exception e, IExecutorExceptionHandler handler) {
        List<Class<?>> list = handler.supportException();
        for (Class clz : list) {
            if (clz.isInstance(e)) {
                return true;
            }
        }
        return false;
    }

    private static String getBody(RequestMeta meta) {
        ResponseEntity<String> entity = meta.getResponseEntity();
        if (entity == null) {
            return null;
        }
        return entity.getBody();
    }


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


    /**
     * 判断是否是 异步接口
     * <p> author: whmmm </p>
     * <p> date  : 2023-03-09 09:34 </p>
     *
     * @param type -
     * @return -
     */
    public static boolean isFuture(Type type) {
        return type.getTypeName().startsWith("java.util.concurrent.Future");
    }

    @SneakyThrows
    protected RequestMeta parseTemplate(DeclareClient client, Object[] args, RequestMetaTemplate template) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> uriMap = new HashMap<>();
        // 解析 template
        RequestMeta meta = new RequestMeta();
        meta.setHttpMethod(template.getHttpMethod());
        meta.setUriVariables(uriMap);
        meta.setHeaders(template.getHeaderMap());
        meta.setAsync(isFuture(template.getReturnType()));
        meta.setReturnType(this.getAsyncReturnType(meta.isAsync(), template.getReturnType()));
        sb.append(template.getUrl());
        Map<String, Object> queryMap = new HashMap<>();

        if (args != null) {
            // 反射 method.getParameters 返回值可能为 null
            for (int i = 0; i < args.length; i++) {
                RequestMetaTemplate.ParamInfo info = template.getParamInfoMap().get(i);
                Object arg = args[i];
                if (info.isBody()) {
                    meta.setBody(arg);
                } else {
                    PathVariable variable = info.getPathVariable();
                    if (variable != null) {
                        uriMap.put(variable.value(), arg);
                    }
                    RequestParam requestParam = info.getRequestParam();
                    if (requestParam != null) {
                        String value = requestParam.value();
                        if ("".equals(value)) {
                            this.setQueryMapFromObject(queryMap, arg);
                        } else {
                            queryMap.put(value, arg);
                        }
                    }
                }
            }
        }
        meta.setQueryMap(queryMap);

        meta.setUrl(
            meta.generateUrl(sb, queryMap)
        );

        return meta;
    }

    /**
     * 从一个对象中设置 url 参数
     * <p> author: whmmm </p>
     * <p> date  : 2023-03-10 09:18 </p>
     *
     * @param queryMap url 中的参数
     * @param arg      Object 对象参数
     * @throws Exception 反射异常
     */
    private void setQueryMapFromObject(Map<String, Object> queryMap, Object arg) throws Exception {
        List<Field> fields = ReflectUtil.getAllFields(arg.getClass());
        for (Field field : fields) {
            field.setAccessible(true);
            queryMap.put(field.getName(), field.get(arg));
        }
    }

    private RequestMetaTemplate getTemplate(String methodString,
                                            DeclareClient client,
                                            Method method,
                                            Object[] args) {

        RequestMetaTemplate template = TEMPLATE_CACHE.get(methodString);
        if (template != null) {
            return template;
        }

        template = new RequestMetaTemplate();
        template.setReturnType(method.getGenericReturnType());

        RequestMapping requestMapping = AnnotatedElementUtils.getMergedAnnotation(method, RequestMapping.class);
        RequestMethod requestMethod = requestMapping.method()[0];

        String[] headers = requestMapping.headers();
        if (headers != null && headers.length > 0) {
            for (String header : headers) {
                String[] split = header.split(":");
                template.getHeaderMap().put(split[0].trim(), split[1].trim());
            }
        }

        template.setHttpMethod(HttpMethod.resolve(requestMethod.name()));
        template.setUrl(
            client.getBaseUrl() + requestMapping.value()[0]
        );

        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter arg = parameters[i];
            RequestMetaTemplate.ParamInfo info = new RequestMetaTemplate.ParamInfo();
            RequestParam param = arg.getAnnotation(RequestParam.class);
            PathVariable pathVariable = arg.getAnnotation(PathVariable.class);

            info.setRequestParam(param);
            info.setPathVariable(pathVariable);


            template.putParam(i, info);
        }

        TEMPLATE_CACHE.put(methodString, template);
        return template;
    }


    /**
     * 获取异步方法的返回值类型
     * <p> author: whmmm </p>
     * <p> date  : 2023-03-09 12:22 </p>
     *
     * @param async      是否是异步
     * @param returnType 返回值类型
     */
    public Type getAsyncReturnType(boolean async,
                                   Type returnType) {
        if (!async) {
            return returnType;
        }

        ParameterizedType type = (ParameterizedType) returnType;
        Type[] arguments = type.getActualTypeArguments();
        if (arguments != null && arguments.length > 0) {
            return arguments[0];
        }
        return null;
    }
}
