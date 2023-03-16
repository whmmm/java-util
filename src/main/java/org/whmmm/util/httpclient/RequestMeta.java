package org.whmmm.util.httpclient;


import lombok.Data;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * <p><b> ----------------------- </b></p>
 * <p><b> author: whmmm           </b></p>
 * <p><b> date  : 2023/3/9 9:09 </b></p>
 *
 * @author whmmm
 */
@Data
public class RequestMeta implements Serializable {
    private String url;
    private HttpMethod httpMethod;
    private Object body;
    private Map<String, ?> uriVariables;
    private Map<String, Object> headers;
    private Type returnType;
    private boolean isAsync;
    private Map<String, Object> queryMap;
    /**
     * 响应信息, 可以为 {@code null}
     */
    @Nullable
    private ResponseEntity<String> responseEntity;


    /**
     * 生成 url
     *
     * @param sb       基础的 url
     * @param queryMap get url 参数 map
     * @return -
     */
    public String generateUrl(StringBuilder sb,
                              Map<String, Object> queryMap) {
        boolean hasQuery = sb.indexOf("?") != -1;

        StringBuilder temp = new StringBuilder();
        for (Map.Entry<String, Object> entry : queryMap.entrySet()) {
            Object value = entry.getValue();
            if (value == null) {
                // 生成 url 时, 不处理 null 值
                continue;
            }
            temp.append("&")
                .append(entry.getKey())
                .append("=")
                .append(value.toString());
        }
        if (hasQuery) {
            sb.append(temp);
        } else {
            // stringBuilder 0,1 是替换第一个字符
            sb.append(
                temp.replace(0, 1, "?")
            );
        }

        return sb.toString();
    }

    /**
     * 生成 url
     *
     * @param sb 基础的 url 信息
     * @return -
     */
    public String generateUrl(StringBuilder sb) {
        return this.generateUrl(sb, this.getQueryMap());
    }
}
