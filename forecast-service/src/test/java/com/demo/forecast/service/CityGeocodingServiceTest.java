package com.demo.forecast.service;

import com.demo.forecast.excpetion.DataNotFoundException;
import com.demo.forecast.model.City;
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
public class CityGeocodingServiceTest {

    @Mock
    private SimpleApiCaller simpleApiCaller;
    @InjectMocks
    private CityGeocodingService geocodingService;
    private City[] cities;

    @Before
    public void init() throws IOException {
        cities = TestUtils
                .mapFromJson(TestUtils.getString("testobjects/geocoding/Cities.json"),
                        City[].class);
    }

    @Test
    public void whenGetCoordinates_thenOK() {
        when(simpleApiCaller.generateOpenWeatherUrl(null, "Milan", null)).thenReturn("Milan");
        when(simpleApiCaller.getCall("Milan", City[].class)).thenReturn(cities);
        City city = geocodingService.getCoordinates("Milan");
        Assert.assertEquals(city.getLatitude(), Double.valueOf(45.4643));
        Assert.assertEquals(city.getLongitude(), Double.valueOf(9.1895));
    }

    @Test(expected = DataNotFoundException.class)
    public void whenGetCoordinates_thenDataNotFoundException() {
        when(simpleApiCaller.generateOpenWeatherUrl(null, "Orka", null)).thenReturn("Orka");
        when(simpleApiCaller.getCall("Orka", City[].class)).thenReturn(new City[]{});
        geocodingService.getCoordinates("Orka");
    }
}
