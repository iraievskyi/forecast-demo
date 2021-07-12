package com.demo.forecast.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ForecastResponseItem {
    private String date;
    private Double minTemp;
    private Double maxTemp;
    private Double averageTemp;
    private Integer humidity;

    public ForecastResponseItem(Long localDateTime, Double minTemp, Double maxTemp, Double averageTemp,
                                Integer humidity, String zoneId) {
        BigDecimal bd = new BigDecimal(averageTemp).setScale(2, RoundingMode.HALF_UP);

        this.date = formatLocalDateTime(localDateTime, zoneId);
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.averageTemp = bd.doubleValue();
        this.humidity = humidity;
    }

    private String formatLocalDateTime(Long seconds, String zoneId) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(seconds), ZoneId.of(zoneId))
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
