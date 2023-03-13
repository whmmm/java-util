package org.whmmm.util.httpclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p><b> ----------------------- </b></p>
 * <p><b> author: whmmm           </b></p>
 * <p><b> date  : 2023/3/9 9:07 </b></p>
 *
 * @author whmmm
 */
public class DeclareClientFactory {

    public static final RestTemplate REST_TEMPLATE = defaultRestTemplate();
    public static final RequestExecutor REFLECT_EXECUTOR = requestExecutor();

    private static final int MAX_TOTAL = 300;
    private static final int MAX_PER_ROUTE = 30;
    private static final int POOL_CONNECT_TIMEOUT = 5 * 1000; // 验证是否连接时间

    private static final int CONNECTION_REQUEST_TIMEOUT = 10 * 1000; // 指从连接池获取连接的timeout超出预设时间  10 秒
    private static final int CONNECTION_TIMEOUT = 10 * 1000;// 指客户端和服务器建立连接的timeout. 超过设定时间,就会报错 connectionrequesttimeout
    private static final int SOCKET_TIMEOUT = 180 * 1000; // 指客户端从服务器读取数据的timeout超出预期设定时间，超出后会抛出SocketTimeOutException
    private static final int RETRY_COUNT = 3; // 重试次数

    /**
     * 创建一个默认的 {@link RestTemplate} , 它的编码是 {@code utf-8}
     * <br> restTemplate 这里有个坑, 如果没有用 spring mvc 配置注入, 那么编码默认是 {@code ISO-8859-1}, 而不是 {@code utf-8}
     * <br> see https://blog.csdn.net/weixin_51591918/article/details/121027930
     *
     * @return -
     */
    public static RestTemplate defaultRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        // RestTemplate 不支持 NGINX 的 gzip
        // see https://stackoverflow.com/questions/42621547/jackson-error-illegal-character-only-regular-white-space-allowed-when-parsi
        // restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        // restTemplate.setInterceptors(Collection2.arrayList(new LoggingInterceptor()));
        restTemplate.setRequestFactory(httpClientFactory());

        // restTemplate 这里有个坑, 如果没有用 spring mvc 配置注入, 那么编码默认是 ISO-8859-1, 而不是 utf-8
        // https://blog.csdn.net/weixin_51591918/article/details/121027930
        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof StringHttpMessageConverter) {
                StringHttpMessageConverter httpMsg = (StringHttpMessageConverter) converter;
                Charset charset = httpMsg.getDefaultCharset();
                if (!charset.name().equals(StandardCharsets.UTF_8.name())) {
                    httpMsg.setDefaultCharset(StandardCharsets.UTF_8);
                }
                break;
            }
        }
        /* ClientHttpRequestFactory factory = restTemplate.getRequestFactory();
        if (factory instanceof HttpComponentsClientHttpRequestFactory) {
            setHttpClientFactory((HttpComponentsClientHttpRequestFactory) factory);
        } */

        return restTemplate;
    }

    public static RequestExecutor requestExecutor() {
        return new RequestExecutor();
    }

    public static Map<String, Object> defaultHeaders() {
        HashMap<String, Object> header = new HashMap<>();
        header.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        header.put("Accept-Charset", "gbk");
        return header;
        /*return Collection2.hashMap(
            , MediaType.APPLICATION_JSON_UTF8_VALUE
        );*/
    }

    public static Gson gson() {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }


    /**
     * 设置重试机制 (Apache HttpClient)<br/>
     * <p color=red> 不设置重试有时会报 {@code Connection reset} 这个错误</p>
     * https://www.jianshu.com/p/3f8322950f37
     * <pre>{@code
     * httpclient的参数maxPerRoute及MaxTotal
     * max-total：连接池里的最大连接数
     * default-max-per-route：每个route默认最大连接数，这里route指的是域名
     * max-per-route：指定路由的最大连接数
     *
     * }</pre>
     *
     * @param factory -
     */
    private static HttpComponentsClientHttpRequestFactory httpClientFactory() {

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();

        connectionManager.setMaxTotal(MAX_TOTAL);
        connectionManager.setDefaultMaxPerRoute(MAX_PER_ROUTE);
        // 设置校验机制（不百分百有效）
        //当每次拿到链接后会校验链接是否还建立，在validateAfterInactivity时间内不重复校验（默认两秒），比如，validateAfterInactivity设置的是5s，上次使用链接是在1s前，则不会校验直接使用 httpClientConnectionManager.setValidateAfterInactivity(5000);
        connectionManager.setValidateAfterInactivity(POOL_CONNECT_TIMEOUT);

        // 参考 https://blog.csdn.net/u010886217/article/details/106869268/
        RequestConfig requestConfig = RequestConfig.custom()
                                                   .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                                                   .setConnectTimeout(CONNECTION_TIMEOUT)
                                                   .setSocketTimeout(SOCKET_TIMEOUT)
                                                   .build();

        CloseableHttpClient result = HttpClientBuilder
            .create()
            .setConnectionManager(connectionManager)
            .setDefaultRequestConfig(requestConfig)
            .setRetryHandler(new CustomRetryHandler(RETRY_COUNT))
            .build();

        factory.setHttpClient(result);

        return factory;
    }

}
