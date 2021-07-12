package com.demo.forecast.service;

import com.demo.forecast.excpetion.DataNotFoundException;
import com.demo.forecast.model.City;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class CityGeocodingServiceIT {

    @Autowired
    private CityGeocodingService geocodingService;

    @Test
    public void whenGetCoordinates_thenOK() {
        City city = geocodingService.getCoordinates("Milan");
        Assert.assertEquals(city.getLatitude(), Double.valueOf(45.4643));
        Assert.assertEquals(city.getLongitude(), Double.valueOf(9.1895));
    }

    @Test(expected = DataNotFoundException.class)
    public void whenGetCoordinates_thenDataNotFoundException() {
        geocodingService.getCoordinates("Orka");
    }
}
