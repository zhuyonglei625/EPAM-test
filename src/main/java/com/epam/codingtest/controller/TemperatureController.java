package com.epam.codingtest.controller;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.epam.codingtest.dto.TemperatureDto;
import com.epam.codingtest.service.TemperatureService;
import com.epam.codingtest.util.Json;
import com.epam.codingtest.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/epam/coding-test/temperature")
public class TemperatureController {
    private static JSONObject allProvinces;

    static {
        String result =  HttpUtil.get("http://www.weather.com.cn/data/city3jdata/china.html", CharsetUtil.CHARSET_UTF_8);
        allProvinces = JSONUtil.parseObj(result);

    }

    @Autowired
    private TemperatureService temperatureService;


    @RequestMapping("/getTemperature")
    @ResponseBody
    public Json getTemperate(@RequestBody TemperatureDto dto){
        Json json = new Json();
        try {
            if (Util.isEmpty(dto.getCity())||Util.isEmpty(dto.getCounty())||Util.isEmpty(dto.getProvince())){
                json.setCode("20002");
                json.setMsg("參數不完整");
                json.setSuccess(false);
                return json;
            }
            if (!allProvinces.containsKey(dto.getProvince())){
                json.setCode("20003");
                json.setMsg("您输入的省份代码错误，请您确认");
                json.setSuccess(false);
                return json;
            }
            Optional<Integer> temp = temperatureService.getTemperature(dto);
            System.out.print(temp.get().intValue());
            Integer integer = temp.get().intValue();
            if(integer.compareTo(10000) > 0 ){
                json.setSuccess(false);
                json.setMsg("查询异常，请您稍后再试！");
                json.setCode("20004");
                return json;
            }
            json.setSuccess(true);
            json.setMsg("查询成功");
            json.setCode("20001");
            json.setData(temp);
            return json;
        }catch (Exception e){
            json.setSuccess(true);
            json.setCode("系统错误，请您稍后再试");
            json.setCode("20002");
            return json;
        }
    }
}
