package com.demo.forecast.service;

import com.demo.forecast.excpetion.DataNotFoundException;
import com.demo.forecast.model.ForecastResponseItem;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ForecastServiceIT {

    @Autowired
    private ForecastService forecastService;

    @Test
    public void whenGetForecastOutsideWorkingTime_thenOK() {
        List<ForecastResponseItem> responseItems = forecastService
                .getForecastOutsideWorkingTime("Milan")
                .getForecasts();
        Assert.assertTrue(responseItems.size() > 0);
    }

    @Test(expected = DataNotFoundException.class)
    public void whenGetForecastOutsideWorkingTime_thenDataNotFoundException() {
        forecastService.getForecastOutsideWorkingTime("Orka");

    }

    @Test
    public void whenGetForecastWithinWorkingTime_thenOK() {
        List<ForecastResponseItem> responseItems = forecastService
                .getForecastDuringWorkingTime("Milan")
                .getForecasts();
        Assert.assertTrue(responseItems.size() > 0);
    }

    @Test(expected = DataNotFoundException.class)
    public void whenGetForecastWithinWorkingTime_thenDataNotFoundException() {
        forecastService.getForecastDuringWorkingTime("Orka");
    }

    @Test
    public void whenGetForecastAll_thenOK() {
        List<ForecastResponseItem> responseItems = forecastService
                .getEntireForecast("Milan")
                .getForecasts();
        Assert.assertEquals(responseItems.size(), 24);
    }

    @Test(expected = DataNotFoundException.class)
    public void whenGetForecastAll_thenDataNotFoundException() {
        forecastService.getEntireForecast("Orka");

    }
}
