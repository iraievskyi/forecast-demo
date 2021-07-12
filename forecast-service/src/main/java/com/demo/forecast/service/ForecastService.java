package com.demo.forecast.service;

import com.demo.forecast.model.City;
import com.demo.forecast.model.Forecast;
import com.demo.forecast.model.ForecastResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

@Service
@RefreshScope
public class ForecastService {
    private final String baseUrl;
    private final String apiKey;

    private final CityGeocodingService geocodingService;
    private final ForecastFilteringService filteringService;
    private final SimpleApiCaller simpleApiCaller;

    public ForecastService(@Value("${one-call.base-url}") String baseUrl, @Value("${api-key}") String apiKey, CityGeocodingService geocodingService,
                           ForecastFilteringService filteringService, SimpleApiCaller simpleApiCaller) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.geocodingService = geocodingService;
        this.filteringService = filteringService;
        this.simpleApiCaller = simpleApiCaller;
    }

    public ForecastResponse getForecastDuringWorkingTime(String cityName) {
        City city = geocodingService.getCoordinates(cityName);
        Forecast forecast = simpleApiCaller
                .getCall(simpleApiCaller.generateForecastCallUrl(baseUrl, apiKey, city), Forecast.class);

        return filteringService.filterItemsWithinWorkingTime(forecast);
    }

    public ForecastResponse getForecastOutsideWorkingTime(String cityName) {
        City city = geocodingService.getCoordinates(cityName);
        Forecast forecast = simpleApiCaller
                .getCall(simpleApiCaller.generateForecastCallUrl(baseUrl, apiKey, city), Forecast.class);

        return filteringService.filterItemsOutsideWorkingTime(forecast);
    }

    public ForecastResponse getEntireForecast(String cityName) {
        City city = geocodingService.getCoordinates(cityName);
        Forecast forecast = simpleApiCaller
                .getCall(simpleApiCaller.generateForecastCallUrl(baseUrl, apiKey, city), Forecast.class);

        return filteringService.entireForecast(forecast);
    }
}
