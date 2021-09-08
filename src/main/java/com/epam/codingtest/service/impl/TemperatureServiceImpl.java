package com.epam.codingtest.service.impl;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.epam.codingtest.dto.TemperatureDto;
import com.epam.codingtest.service.TemperatureService;
import com.google.common.util.concurrent.RateLimiter;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 溫度接口impl
 */
@Service
public class TemperatureServiceImpl implements TemperatureService {

    private static RateLimiter limiter = RateLimiter.create(100);

    /**
     * 获取气温
     *  TemperatureDto dto 参数bean
     * @return  气温
     */
    @Override
    @Retryable(value = {Exception.class},maxAttempts = 3,backoff = @Backoff(delay = 2000L, multiplier = 1))
    public Optional<Integer> getTemperature(TemperatureDto dto) {
        limiter.acquire(1);
        return getTemp(dto.getProvince(),dto.getCity(),dto.getCounty());
    }

    /**
     * 获取气温
     * @param province 省代码
     * @param city  市代码
     * @param country   区代码
     * @return  气温
     */
    private Optional<Integer> getTemp(String province, String city, String country){
        String  citiesOfProvince = HttpUtil.get("http://www.weather.com.cn/data/city3jdata/provshi/"+province+".html", CharsetUtil.CHARSET_UTF_8);
        if(!JSONUtil.isJson(citiesOfProvince)){
            return Optional.of(10001);
        }
        String countriesOfCity = HttpUtil.get("http://www.weather.com.cn/data/city3jdata/station/"+province+city+".html", CharsetUtil.CHARSET_UTF_8);
        if(!JSONUtil.isJson(countriesOfCity)){
            return Optional.of(10002);
        }
        String countryWeather = HttpUtil.get("http://www.weather.com.cn/data/sk/"+province+city+country+".html", CharsetUtil.CHARSET_UTF_8);
        if(!JSONUtil.isJson(countryWeather)){
            return Optional.of(10003);
        }else{
            JSONObject jsonObject = JSONUtil.parseObj(countryWeather);
            Object weatherinfo = jsonObject.get("weatherinfo");
            if(!JSONUtil.isNull(weatherinfo)) {
                JSONObject weather = JSONUtil.parseObj(weatherinfo);
                Integer temp = Math.round(Float.parseFloat((String)weather.get("temp")));
                return Optional.of(temp);
            }else{
                return Optional.of(10004);
            }
        }
    }
}
