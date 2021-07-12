package com.demo.forecast.service;

import com.demo.forecast.model.City;
import com.demo.forecast.model.Forecast;
import com.demo.forecast.model.ForecastResponse;
import com.demo.forecast.model.ForecastResponseItem;
import com.demo.forecast.testutils.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ForecastServiceTest {
    @Mock
    private CityGeocodingService geocodingService;
    @Mock
    private ForecastFilteringService filteringService;
    @Mock
    private SimpleApiCaller simpleApiCaller;
    @InjectMocks
    private ForecastService forecastService;

    private City city;

    private Forecast forecast;

    private ForecastResponse forecastResponseWithin;
    private ForecastResponse forecastResponseOutside;
    private ForecastResponse forecastResponseAll;

    @Before
    public void init() throws IOException {
        city = TestUtils
                .mapFromJson(TestUtils.getString("testobjects/forecast/City.json"),
                        City.class);

        forecast = TestUtils
                .mapFromJson(TestUtils.getString("testobjects/forecast/Forecast.json"),
                        Forecast.class);

        forecastResponseWithin = TestUtils
                .mapFromJson(TestUtils.getString("testobjects/response/ForecastWithinWorkingHours.json"),
                        ForecastResponse.class);
        forecastResponseOutside = TestUtils
                .mapFromJson(TestUtils.getString("testobjects/response/ForecastOutsideWorkingHours.json"),
                        ForecastResponse.class);
        forecastResponseAll = TestUtils
                .mapFromJson(TestUtils.getString("testobjects/response/ForecastAll.json"),
                        ForecastResponse.class);
    }

    @Test
    public void whenGetForecastDuringWorkingTime_thenOK() {
        when(geocodingService.getCoordinates("Milan")).thenReturn(city);
        when(simpleApiCaller.generateForecastCallUrl(null, null, city)).thenReturn("Url");
        when(simpleApiCaller.getCall("Url", Forecast.class)).thenReturn(forecast);
        when(filteringService.filterItemsWithinWorkingTime(forecast)).thenReturn(forecastResponseWithin);
        ForecastResponseItem forecastResponseMock = forecastResponseWithin.getForecasts().stream().findFirst().get();

        ForecastResponse forecastResponse = forecastService.getForecastDuringWorkingTime("Milan");
        ForecastResponseItem forecastResponseItem = forecastResponse.getForecasts().stream().findFirst().get();

        Assert.assertEquals("Date: ", forecastResponseItem.getDate(), forecastResponseMock.getDate());
        Assert.assertEquals("MinTemp: ", forecastResponseItem.getMinTemp(), forecastResponseMock.getMinTemp());
        Assert.assertEquals("MaxTemp: ", forecastResponseItem.getMaxTemp(), forecastResponseMock.getMaxTemp());
        Assert.assertEquals("AverageTemp: ", forecastResponseItem.getAverageTemp(),
                forecastResponseMock.getAverageTemp());
        Assert.assertEquals("Humidity: ", forecastResponseItem.getHumidity(),
                forecastResponseMock.getHumidity());
    }

    @Test
    public void whenGetForecastOutsideWorkingTime_thenOK() {
        when(geocodingService.getCoordinates("Milan")).thenReturn(city);
        when(simpleApiCaller.generateForecastCallUrl(null, null, city)).thenReturn("Url");
        when(simpleApiCaller.getCall("Url", Forecast.class)).thenReturn(forecast);
        when(filteringService.filterItemsOutsideWorkingTime(forecast)).thenReturn(forecastResponseOutside);
        ForecastResponseItem forecastResponseMock = forecastResponseOutside.getForecasts().stream().findFirst().get();

        ForecastResponse forecastResponse = forecastService.getForecastOutsideWorkingTime("Milan");
        ForecastResponseItem forecastResponseItem = forecastResponse.getForecasts().stream().findFirst().get();

        Assert.assertEquals("Date: ", forecastResponseItem.getDate(), forecastResponseMock.getDate());
        Assert.assertEquals("MinTemp: ", forecastResponseItem.getMinTemp(), forecastResponseMock.getMinTemp());
        Assert.assertEquals("MaxTemp: ", forecastResponseItem.getMaxTemp(), forecastResponseMock.getMaxTemp());
        Assert.assertEquals("AverageTemp: ", forecastResponseItem.getAverageTemp(),
                forecastResponseMock.getAverageTemp());
        Assert.assertEquals("Humidity: ", forecastResponseItem.getHumidity(),
                forecastResponseMock.getHumidity());
    }

    @Test
    public void whenGetForecastAll_thenOK() {
        when(geocodingService.getCoordinates("Milan")).thenReturn(city);
        when(simpleApiCaller.generateForecastCallUrl(null, null, city)).thenReturn("Url");
        when(simpleApiCaller.getCall("Url", Forecast.class)).thenReturn(forecast);
        when(filteringService.entireForecast(forecast)).thenReturn(forecastResponseAll);
        ForecastResponseItem forecastResponseMock = forecastResponseAll.getForecasts().stream().findFirst().get();

        ForecastResponse forecastResponse = forecastService.getEntireForecast("Milan");
        ForecastResponseItem forecastResponseItem = forecastResponse.getForecasts().stream().findFirst().get();

        Assert.assertEquals("Date: ", forecastResponseItem.getDate(), forecastResponseMock.getDate());
        Assert.assertEquals("MinTemp: ", forecastResponseItem.getMinTemp(), forecastResponseMock.getMinTemp());
        Assert.assertEquals("MaxTemp: ", forecastResponseItem.getMaxTemp(), forecastResponseMock.getMaxTemp());
        Assert.assertEquals("AverageTemp: ", forecastResponseItem.getAverageTemp(),
                forecastResponseMock.getAverageTemp());
        Assert.assertEquals("Humidity: ", forecastResponseItem.getHumidity(),
                forecastResponseMock.getHumidity());
    }

}
