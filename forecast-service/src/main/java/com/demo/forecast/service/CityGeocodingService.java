package com.demo.forecast.service;

import com.demo.forecast.excpetion.DataNotFoundException;
import com.demo.forecast.model.City;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RefreshScope
public class CityGeocodingService {
    private final String baseUrl;
    private final String apiKey;

    private final SimpleApiCaller simpleApiCaller;

    public CityGeocodingService(@Value("${geocoding.base-url}") String baseUrl,
                                @Value("${api-key}") String apiKey, SimpleApiCaller simpleApiCaller) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.simpleApiCaller = simpleApiCaller;
    }

    public City getCoordinates(String cityName) {
        String url = simpleApiCaller.generateOpenWeatherUrl(baseUrl, cityName, apiKey);
        return Arrays.stream(simpleApiCaller
                .getCall(url, City[].class))
                .findAny()
                .orElseThrow(() -> new DataNotFoundException(String.format("City %s not found", cityName)));
    }
}
