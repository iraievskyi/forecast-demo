package com.demo.forecast.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForecastItem {
    @JsonProperty("dt")
    private Long timestamp;
    @JsonProperty("temp")
    private Double temperature;
    private Integer humidity;
}
