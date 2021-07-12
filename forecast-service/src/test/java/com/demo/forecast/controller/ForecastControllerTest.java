package com.demo.forecast.controller;

import com.demo.forecast.excpetion.DataNotFoundException;
import com.demo.forecast.model.ForecastResponse;
import com.demo.forecast.service.ForecastService;
import com.demo.forecast.testutils.TestUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.io.IOException;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ForecastController.class)
public class ForecastControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ForecastService forecastService;

    private ForecastResponse forecastResponseWithin;
    private ForecastResponse forecastResponseOutside;
    private ForecastResponse forecastResponseAll;


    @Before
    public void init() throws IOException {

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
    public void whenWithinWorkingHours_thenStatusIsOk() throws Exception {
        given(forecastService.getForecastDuringWorkingTime("Milan")).willReturn(forecastResponseWithin);

        this.mockMvc.perform(get("/v1/forecasts/within-working-hours")
                .param("city", "Milan"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$.forecasts", hasSize(10)))
                .andExpect(jsonPath("$.forecasts[0].humidity", Matchers.is(58)));
    }

    @Test
    public void whenWithinWorkingHours_thenDataNotFoundException() throws Exception {
        given(forecastService.getForecastDuringWorkingTime("Milan"))
                .willThrow(new DataNotFoundException("No data found"));

        this.mockMvc.perform(get("/v1/forecasts/within-working-hours")
                .param("city", "Milan"))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> Assert.assertTrue(result.getResolvedException()
                        instanceof DataNotFoundException))
                .andExpect(jsonPath("$.details[0]", Matchers.is("No data found")));
    }

    @Test
    public void whenWithinWorkingHours_thenMissingServletRequestParameterException() throws Exception {
        this.mockMvc.perform(get("/v1/forecasts/within-working-hours"))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> Assert.assertTrue(result.getResolvedException()
                        instanceof MissingServletRequestParameterException));
    }

    @Test
    public void whenWithinWorkingHours_thenHttpRequestMethodNotSupportedException() throws Exception {
        this.mockMvc.perform(post("/v1/forecasts/within-working-hours")
                .param("city", "Milan"))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> Assert.assertTrue(result.getResolvedException()
                        instanceof HttpRequestMethodNotSupportedException));
    }


    @Test
    public void whenOutsideWorkingHours_thenStatusIsOk() throws Exception {
        given(forecastService.getForecastOutsideWorkingTime("Milan")).willReturn(forecastResponseOutside);

        this.mockMvc.perform(get("/v1/forecasts/outside-working-hours")
                .param("city", "Milan"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$.forecasts", hasSize(14)))
                .andExpect(jsonPath("$.forecasts[0].date", Matchers.is("2021-07-11T22:00:00")))
                .andExpect(jsonPath("$.forecasts[0].minTemp", Matchers.is(23.72)))
                .andExpect(jsonPath("$.forecasts[0].maxTemp", Matchers.is(23.96)))
                .andExpect(jsonPath("$.forecasts[0].averageTemp", Matchers.is(23.84)))
                .andExpect(jsonPath("$.forecasts[0].humidity", Matchers.is(56)));
    }

    @Test
    public void whenOutsideWorkingHours_thenDataNotFoundException() throws Exception {
        given(forecastService.getForecastOutsideWorkingTime("Milan"))
                .willThrow(new DataNotFoundException("No data found"));

        this.mockMvc.perform(get("/v1/forecasts/outside-working-hours")
                .param("city", "Milan"))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> Assert.assertTrue(result.getResolvedException()
                        instanceof DataNotFoundException))
                .andExpect(jsonPath("$.details[0]", Matchers.is("No data found")));
    }

    @Test
    public void whenOutsideWorkingHours_thenMissingServletRequestParameterException() throws Exception {
        this.mockMvc.perform(get("/v1/forecasts/outside-working-hours"))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> Assert.assertTrue(result.getResolvedException()
                        instanceof MissingServletRequestParameterException));
    }

    @Test
    public void whenOutsideWorkingHours_thenHttpRequestMethodNotSupportedException() throws Exception {
        this.mockMvc.perform(post("/v1/forecasts/outside-working-hours")
                .param("city", "Milan"))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> Assert.assertTrue(result.getResolvedException()
                        instanceof HttpRequestMethodNotSupportedException));
    }

    @Test
    public void whenAll_thenStatusIsOk() throws Exception {
        given(forecastService.getEntireForecast("Milan")).willReturn(forecastResponseAll);

        this.mockMvc.perform(get("/v1/forecasts/all")
                .param("city", "Milan"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$.forecasts", hasSize(24)))
                .andExpect(jsonPath("$.forecasts[1].date", Matchers.is("2021-07-12T00:00:00")))
                .andExpect(jsonPath("$.forecasts[1].minTemp", Matchers.is(23.24)))
                .andExpect(jsonPath("$.forecasts[1].maxTemp", Matchers.is(23.65)))
                .andExpect(jsonPath("$.forecasts[1].averageTemp", Matchers.is(23.45)))
                .andExpect(jsonPath("$.forecasts[1].humidity", Matchers.is(57)));
    }

    @Test
    public void whenAll_thenDataNotFoundException() throws Exception {
        given(forecastService.getEntireForecast("Milan"))
                .willThrow(new DataNotFoundException("No data found"));

        this.mockMvc.perform(get("/v1/forecasts/all")
                .param("city", "Milan"))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> Assert.assertTrue(result.getResolvedException()
                        instanceof DataNotFoundException))
                .andExpect(jsonPath("$.details[0]", Matchers.is("No data found")));
    }

    @Test
    public void whenAll_thenMissingServletRequestParameterException() throws Exception {
        this.mockMvc.perform(get("/v1/forecasts/all"))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> Assert.assertTrue(result.getResolvedException()
                        instanceof MissingServletRequestParameterException));
    }

    @Test
    public void whenAll_thenHttpRequestMethodNotSupportedException() throws Exception {
        this.mockMvc.perform(post("/v1/forecasts/all")
                .param("city", "Milan"))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> Assert.assertTrue(result.getResolvedException()
                        instanceof HttpRequestMethodNotSupportedException));
    }

}
