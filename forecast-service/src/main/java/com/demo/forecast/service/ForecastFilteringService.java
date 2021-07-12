package com.demo.forecast.service;

import com.demo.forecast.model.Forecast;
import com.demo.forecast.model.ForecastItem;
import com.demo.forecast.model.ForecastResponse;
import com.demo.forecast.model.ForecastResponseItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import static com.demo.forecast.utils.ForecastUtils.doubleAverage;

@Service
@RefreshScope
public class ForecastFilteringService {

    @Value("${start.hours}")
    Integer startHours;
    @Value("${start.minutes}")
    Integer startMinutes;
    @Value("${end.hours}")
    Integer endHours;
    @Value("${end.minutes}")
    Integer endMinutes;

    public ForecastResponse filterItemsWithinWorkingTime(Forecast forecast) {
        var duringWorkingHours = getForecastFilteredItems(forecast, betweenStartAndEndTimePredicate());
        var responseItems = getForecastResponseItems(forecastResponseItemBiFunction(forecast.getZoneId()),
                duringWorkingHours);

        return new ForecastResponse(responseItems);
    }

    public ForecastResponse filterItemsOutsideWorkingTime(Forecast forecast) {
        var duringWorkingHours = getForecastFilteredItems(forecast,
                betweenStartAndEndTimePredicate().negate());
        var responseItems = getForecastResponseItems(forecastResponseItemBiFunction(forecast.getZoneId()),
                duringWorkingHours);

        return new ForecastResponse(responseItems);
    }

    public ForecastResponse entireForecast(Forecast forecast) {
        var duringWorkingHours = Arrays.asList(forecast.getItems());
        var responseItems = getForecastResponseItems(forecastResponseItemBiFunction(forecast.getZoneId()),
                duringWorkingHours);

        return new ForecastResponse(responseItems);
    }

    List<ForecastItem> getForecastFilteredItems(Forecast forecast, Predicate<LocalTime> betweenStartAndEnd) {
        return Arrays.stream(forecast.getItems())
                .filter(forecastItem -> betweenStartAndEnd
                        .test(LocalTime.ofInstant(Instant.ofEpochSecond(forecastItem.getTimestamp()),
                                ZoneId.of(forecast.getZoneId()))))
                .collect(Collectors.toList());
    }

    List<ForecastResponseItem> getForecastResponseItems(BiFunction<ForecastItem, ForecastItem,
            ForecastResponseItem> responseItemGeneration, List<ForecastItem> duringWorkingHours) {
        return IntStream.iterate(0,
                count -> count < (duringWorkingHours.size() % 2 == 0 ? duringWorkingHours.size() :
                        duringWorkingHours.size() - 1), n -> n + 2)
                .mapToObj(next -> responseItemGeneration
                        .apply(duringWorkingHours.get(next), duringWorkingHours.get(next + 1)))
                .collect(Collectors.toList());
    }

    BiFunction<ForecastItem, ForecastItem, ForecastResponseItem> forecastResponseItemBiFunction(String zoneId) {
        return (a, b) ->
                new ForecastResponseItem(a.getTimestamp(), Double.min(a.getTemperature(), b.getTemperature()),
                        Double.max(a.getTemperature(), b.getTemperature()),
                        doubleAverage(DoubleStream.of(a.getTemperature(), b.getTemperature())),
                        (int) doubleAverage(DoubleStream.of(a.getHumidity(), a.getHumidity())), zoneId);
    }

    Predicate<LocalTime> betweenStartAndEndTimePredicate() {
        Predicate<LocalTime> isAfterStart = start -> start
                .isAfter(LocalTime.of(startHours, startMinutes)) ||
                start.equals(LocalTime.of(startHours, startMinutes));

        return isAfterStart
                .and(end -> end.isBefore(LocalTime.of(endHours, endMinutes).plusSeconds(3601)));
    }
}
