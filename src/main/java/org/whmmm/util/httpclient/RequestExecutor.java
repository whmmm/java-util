package org.whmmm.util.httpclient;

import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * 真正用于发送请求, 结果解析<br/>
 * 这是接口默认 {@link IRequestExecutor}的默认实现, 基于 {@link RestTemplate}
 * <p><b> ----------------------- </b></p>
 * <p><b> author: whmmm           </b></p>
 * <p><b> date  : 2023/3/9 9:08 </b></p>
 *
 * @author whmmm
 */
@Slf4j
@Data
class RequestExecutor implements Serializable, IRequestExecutor {

    private RestTemplate restTemplate = DeclareClientFactory.REST_TEMPLATE;
    private static final Gson GSON = DeclareClientFactory.gson();
    private boolean debug = true;
    private long slowApiTime = 800;
    private Logger logger = LoggerFactory.getLogger(RequestExecutor.class);

    /**
     * 执行 {@link RestTemplate} 的 {@code exchange } 方法
     *
     * @param meta 请求元数据信息
     * @return -
     */
    @Nullable
    public Object executeRequest(RequestMeta meta) {
        HttpHeaders headers = new HttpHeaders();
        Map<String, Object> headerMap = meta.getHeaders();
        if (headerMap != null) {
            for (Map.Entry<String, Object> entry : headerMap.entrySet()) {
                headers.set(
                    entry.getKey(),
                    entry.getValue().toString()
                );
            }
        }

        HttpEntity<Object> httpEntity = new HttpEntity<>(meta.getBody(), headers);

        boolean isResponseEntity = ReflectUtil.isGenericType(
            ResponseEntity.class,
            meta.getReturnType()
        );

        if (isResponseEntity) {
            // 有时候就想返回 responseEntity
            // 如果返回值直接是 responseEntity . 那么需要做特殊处理
            TypeRef typeRef = new TypeRef(ReflectUtil.getFirstGenericType(meta.getReturnType()));
            // 使用 spring 的解析
            return restTemplate.exchange(
                meta.getUrl(),
                meta.getHttpMethod(),
                httpEntity,
                typeRef,
                meta.getUriVariables()
            );
        } else {
            // 使用 gson 解析
            ResponseEntity<String> exchange = restTemplate.exchange(
                meta.getUrl(),
                meta.getHttpMethod(),
                httpEntity,
                String.class,
                meta.getUriVariables()
            );

            meta.setResponseEntity(exchange);

            String body = exchange.getBody();
            Type returnType = meta.getReturnType();
            if (returnType.equals(String.class)) {
                return body;
            } else {
                return GSON.fromJson(body, returnType);
            }
        }

    }
}
