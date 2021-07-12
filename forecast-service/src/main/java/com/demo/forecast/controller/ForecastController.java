package com.demo.forecast.controller;

import com.demo.forecast.model.ForecastResponse;
import com.demo.forecast.service.ForecastService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/forecasts")
public class ForecastController {

    private final ForecastService forecastService;

    public ForecastController(ForecastService forecastService) {
        this.forecastService = forecastService;
    }

    @GetMapping(value = "/within-working-hours")
    public ResponseEntity<ForecastResponse> getForecastWithinWorkingHours(@RequestParam("city") String city) {
        return ResponseEntity.ok(forecastService.getForecastDuringWorkingTime(city));
    }

    @GetMapping(value = "/outside-working-hours")
    public ResponseEntity<ForecastResponse> getForecastOutsideWorkingHours(@RequestParam("city") String city) {
        return ResponseEntity.ok(forecastService.getForecastOutsideWorkingTime(city));
    }

    @GetMapping(value = "/all")
    public ResponseEntity<ForecastResponse> getForecast(@RequestParam("city") String city) {
        return ResponseEntity.ok(forecastService.getEntireForecast(city));
    }
}
