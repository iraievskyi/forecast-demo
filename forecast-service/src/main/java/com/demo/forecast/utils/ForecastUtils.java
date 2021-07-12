package com.demo.forecast.utils;

import java.util.stream.DoubleStream;

public class ForecastUtils {
    public static double doubleAverage(DoubleStream stream) {
        return stream.average().getAsDouble();
    }
}
