package org.whmm.util.test.httpclient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * see https://www.tianqiapi.com/
 * <p> -------------------------- </p>
 * <p> *** author: whmmm | date: 2023/3/12 17:05*** </p>
 *
 * @author whmmm
 */
public interface WeatherService {

    /**
     * 返回值为 string , 自己解析结果
     *
     * @return -
     */
    @GetMapping("")
    String getWeatherV1();

    /**
     * 指定返回值类型
     *
     * @param cityName -参数: cityName
     * @return -
     */
    @GetMapping("")
    Object getWeatherV2(@RequestParam("cityName") String cityName);
}
