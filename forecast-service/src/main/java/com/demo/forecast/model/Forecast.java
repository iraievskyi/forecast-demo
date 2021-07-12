package com.demo.forecast.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Forecast {
    @JsonProperty("timezone")
    private String zoneId;
    @JsonProperty("hourly")
    private ForecastItem[] items;
}
