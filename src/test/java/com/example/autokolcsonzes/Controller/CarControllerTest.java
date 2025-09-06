package com.example.autokolcsonzes.Controller;

import com.example.autokolcsonzes.Model.Car;
import com.example.autokolcsonzes.Service.CarService;
import com.example.autokolcsonzes.Utils.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarService carService;

    private Car sampleCar;

    @BeforeEach
    void setup() {
        sampleCar = new Car();
        sampleCar.setId(1);
        sampleCar.setName("Tesla Model S");
        sampleCar.setPricePerDay(100);
        sampleCar.setActive(true);
    }

    @Test
    void testIndexReturnsIndexPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("admin"));
    }

    @Test
    void testSearchReturnsCars() throws Exception {
        Mockito.when(carService.searchCar("3000-01-01","3000-01-05"))
                .thenReturn(List.of(sampleCar));

        mockMvc.perform(get("/cars/search")
                        .param("start", "3000-01-01")
                        .param("end", "3000-01-05"))
                .andExpect(status().isOk())
                .andExpect(view().name("searchResult"))
                .andExpect(model().attributeExists("cars"));
    }

    @Test
    void testSearchReturnsErrorIfEmpty() throws Exception {
        Mockito.when(carService.searchCar(any(), any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/cars/search")
                        .param("start", "3000-01-01")
                        .param("end", "3000-01-05"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("errors"));
    }

    @Test
    void testSearchHandlesValidationException() throws Exception {
        Mockito.when(carService.searchCar(any(), any()))
                .thenThrow(new ValidationException(List.of("Invalid date format")));

        mockMvc.perform(get("/cars/search")
                        .param("start", "not-a-date")
                        .param("end", "not-a-date"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("errors"));
    }

    @Test
    void testGetCarWorks() throws Exception {
        Mockito.when(carService.getCar(1)).thenReturn(sampleCar);

        mockMvc.perform(get("/cars/1")
                        .param("start", "3000-01-01")
                        .param("end", "3000-01-05"))
                .andExpect(status().isOk())
                .andExpect(view().name("carRental"))
                .andExpect(model().attributeExists("car"))
                .andExpect(model().attribute("days", 5L)); // inclusive days
    }

    @Test
    void testGetCarReturnsErrorOnInvalidId() throws Exception {
        mockMvc.perform(get("/cars/notANumber")
                        .param("start", "3000-01-01")
                        .param("end", "3000-01-05"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }
}
