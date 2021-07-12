package com.demo.forecast.service;

import com.demo.forecast.model.City;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SimpleApiCaller {
    private final RestTemplate restTemplate;

    public SimpleApiCaller() {
        this.restTemplate = new RestTemplate();
    }

    public <T> T getCall(String url, Class<T> clazz) {
        return restTemplate.getForObject(url, clazz);
    }

    protected String generateOpenWeatherUrl(String baseUrl, String cityName, String apiKey) {
        return String.format(baseUrl, cityName, apiKey);
    }

    protected String generateForecastCallUrl(String baseUrl, String apiKey, City city) {
        return String.format(baseUrl, city.getLatitude(), city.getLongitude(), apiKey);
    }
}
