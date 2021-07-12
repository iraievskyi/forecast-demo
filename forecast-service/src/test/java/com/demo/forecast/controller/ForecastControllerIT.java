package com.demo.forecast.controller;

import com.demo.forecast.excpetion.DataNotFoundException;
import com.demo.forecast.model.ForecastResponse;
import com.demo.forecast.model.ForecastResponseItem;
import com.demo.forecast.service.ForecastService;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MissingServletRequestParameterException;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ForecastControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ForecastService forecastService;

    private ForecastResponse forecastResponse;
    private ForecastResponseItem forecastResponseItem;

    @Test
    public void whenWithinWorkingHours_thenStatusIsOk() throws Exception {

        forecastResponse = forecastService.getForecastDuringWorkingTime("Milan");
        forecastResponseItem = forecastResponse.getForecasts().stream().findFirst().get();

        this.mockMvc.perform(get("/v1/forecasts/within-working-hours")
                .param("city", "Milan"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$.forecasts", hasSize(forecastResponse.getForecasts().size())))
                .andExpect(jsonPath("$.forecasts[0].humidity",
                        Matchers.is(forecastResponseItem.getHumidity())))
                .andExpect(jsonPath("$.forecasts[0].date", Matchers.is(forecastResponseItem.getDate())))
                .andExpect(jsonPath("$.forecasts[0].minTemp", Matchers.is(forecastResponseItem.getMinTemp())))
                .andExpect(jsonPath("$.forecasts[0].maxTemp", Matchers.is(forecastResponseItem.getMaxTemp())))
                .andExpect(jsonPath("$.forecasts[0].averageTemp",
                        Matchers.is(forecastResponseItem.getAverageTemp())));
    }

    @Test
    public void whenWithinWorkingHours_thenDataNotFoundException() throws Exception {

        this.mockMvc.perform(get("/v1/forecasts/within-working-hours")
                .param("city", "Milanggg"))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> Assert.assertTrue(result.getResolvedException()
                        instanceof DataNotFoundException))
                .andExpect(jsonPath("$.message", Matchers.is("Data Not Found")));
    }

    @Test
    public void whenWithinWorkingHours_thenMissingServletRequestParameterException() throws Exception {
        this.mockMvc.perform(get("/v1/forecasts/within-working-hours"))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> Assert.assertTrue(result.getResolvedException()
                        instanceof MissingServletRequestParameterException));
    }


    @Test
    public void whenOutsideWorkingHours_thenStatusIsOk() throws Exception {

        forecastResponse = forecastService.getForecastOutsideWorkingTime("Milan");
        forecastResponseItem = forecastResponse.getForecasts().stream().findFirst().get();

        this.mockMvc.perform(get("/v1/forecasts/outside-working-hours")
                .param("city", "Milan"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$.forecasts", hasSize(forecastResponse.getForecasts().size())))
                .andExpect(jsonPath("$.forecasts[0].humidity",
                        Matchers.is(forecastResponseItem.getHumidity())))
                .andExpect(jsonPath("$.forecasts[0].date", Matchers.is(forecastResponseItem.getDate())))
                .andExpect(jsonPath("$.forecasts[0].minTemp", Matchers.is(forecastResponseItem.getMinTemp())))
                .andExpect(jsonPath("$.forecasts[0].maxTemp", Matchers.is(forecastResponseItem.getMaxTemp())))
                .andExpect(jsonPath("$.forecasts[0].averageTemp",
                        Matchers.is(forecastResponseItem.getAverageTemp())));
    }

    @Test
    public void whenOutsideWorkingHours_thenDataNotFoundException() throws Exception {

        this.mockMvc.perform(get("/v1/forecasts/outside-working-hours")
                .param("city", "Milanggg"))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> Assert.assertTrue(result.getResolvedException()
                        instanceof DataNotFoundException))
                .andExpect(result -> Assert.assertTrue(result.getResolvedException()
                        instanceof DataNotFoundException))
                .andExpect(jsonPath("$.message", Matchers.is("Data Not Found")));
    }

    @Test
    public void whenOutsideWorkingHours_thenMissingServletRequestParameterException() throws Exception {
        this.mockMvc.perform(get("/v1/forecasts/outside-working-hours"))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> Assert.assertTrue(result.getResolvedException()
                        instanceof MissingServletRequestParameterException));
    }
}
