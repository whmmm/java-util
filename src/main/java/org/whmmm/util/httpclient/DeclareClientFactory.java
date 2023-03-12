package org.whmmm.util.httpclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        // restTemplate.setInterceptors(Collection2.arrayList(new LoggingInterceptor()));

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
}
