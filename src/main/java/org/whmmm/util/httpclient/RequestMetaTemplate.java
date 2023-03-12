package org.whmmm.util.httpclient;

import lombok.Data;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * <p><b> ----------------------- </b></p>
 * <p><b> author: whmmm           </b></p>
 * <p><b> date  : 2023/3/9 9:09 </b></p>
 *
 * @author whmmm
 */
@Data
class RequestMetaTemplate implements Serializable {
    /**
     * {@code Map<参数序号, request 请求绑定的参数信息> }
     */
    private Map<Integer, ParamInfo> paramInfoMap = new HashMap<>();
    private final Map<String, Object> headerMap = DeclareClientFactory.defaultHeaders();
    private String url;
    private HttpMethod httpMethod;
    /**
     * {@code request header -- Content-Type }.
     * 不指定时, 默认为 json
     */
    private String contentType = MediaType.APPLICATION_JSON_UTF8_VALUE;

    private Type returnType;

    public void putParam(Integer index,
                         ParamInfo info) {
        paramInfoMap.put(index, info);
    }


    @Data
    public static class ParamInfo {

        RequestParam requestParam;

        PathVariable pathVariable;

        public boolean isBody() {
            return requestParam == null &&
                   pathVariable == null;
        }
    }
}
