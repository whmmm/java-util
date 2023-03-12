package org.whmm.util.test.httpclient;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.whmmm.util.httpclient.DeclareClient;

/**
 * <p> -------------------------- </p>
 * <p> *** author: whmmm | date: 2023/3/12 16:58*** </p>
 *
 * @author whmmm
 */
@Slf4j
public class HttpclientTest {
    @Test
    public void test() {
        log.warn("xxxxxxxxxxxxxxx");

        String uri = "https://v0.yiketianqi.com/api?unescape=1&version=v91&appid=43656176&appsecret=I42og6Lm&ext=&cityid=";
        WeatherService service = DeclareClient.builder()
                                              .target(WeatherService.class, uri);

        String weatherV1 = service.getWeatherV1();
        // System.out.println("");

        Object v2 = service.getWeatherV2("保定");
        System.out.println("");
    }
}
