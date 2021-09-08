package com.epam.codingtest.service;

import com.epam.codingtest.dto.TemperatureDto;

import java.util.Optional;

/**
 *  查询温度接口
 */
public interface TemperatureService {

    Optional<Integer> getTemperature(TemperatureDto dto);
}

