package org.whmmm.util.httpclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpServerErrorException;

/**
 * 用于打印远程接口 (http) 报 500 的情况
 * <p><b> ----------------------- </b></p>
 * <p><b> author: whmmm           </b></p>
 * <p><b> date  : 2023/3/14 11:27 </b></p>
 *
 * @author whmmm
 */
@Slf4j
class HttpServerErrorLogInterceptor implements IRequestExecutorInterceptor {

    @Override
    public void onError(Exception e, RequestMeta meta) {
        if (e instanceof HttpServerErrorException) {
            String errorBody = ((HttpServerErrorException) e).getResponseBodyAsString();
            log.error(
                "url " + meta.getUrl() + " exception !"
            );
            log.error("remote server error:" + errorBody);
        }
    }
}
