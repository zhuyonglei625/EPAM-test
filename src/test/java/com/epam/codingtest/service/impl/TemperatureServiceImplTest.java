package com.epam.codingtest.service.impl;

import com.epam.codingtest.MainApplication;
import com.epam.codingtest.dto.TemperatureDto;
import com.epam.codingtest.service.TemperatureService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={MainApplication.class})
public class TemperatureServiceImplTest  {

    @Autowired
    TemperatureService temperatureService;

    @Test
    public void getTemperature() {
        TemperatureDto dto1 = new TemperatureDto("10119", "04", "01");
        Optional<Integer> t1 = temperatureService.getTemperature(dto1);
        System.out.println("正常测试："+t1);
        TemperatureDto dto2 = new TemperatureDto("156516", "04", "01");
        Optional<Integer> t2 = temperatureService.getTemperature(dto2);
        System.out.println("省编码错误"+t2);
        TemperatureDto dto3 = new TemperatureDto("10119", "1000", "01");
        Optional<Integer> t3 = temperatureService.getTemperature(dto3);
        System.out.println("市编码错误:"+t3);
        TemperatureDto dto4 = new TemperatureDto("10119", "04", "123456");
        Optional<Integer> t4 = temperatureService.getTemperature(dto4);
        System.out.println("区编码错误:"+ t4);
    }

    @Test
    public void getTemplateThread(){
        for (int i = 0; i < 50; i++) {
            TestThread testThread = new TestThread();
            testThread.setName("测试线程"+(i+1));
            testThread.run();
        }
        System.out.println("===========================================");
        for (int i = 0; i < 101; i++) {
            TestThread testThread = new TestThread();
            testThread.setName("测试线程"+i);
            testThread.run();
        }
    }

    class TestThread extends Thread{
        @Override
        public void run() {
            TemperatureDto dto1 = new TemperatureDto("10119", "04", "01");
            Optional<Integer> t1 = temperatureService.getTemperature(dto1);
            System.out.println(this.getName()+"正常测试："+t1);
        }
    }
}
