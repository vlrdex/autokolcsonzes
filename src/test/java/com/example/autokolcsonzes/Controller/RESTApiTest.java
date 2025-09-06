package com.example.autokolcsonzes.Controller;

import com.example.autokolcsonzes.Model.Car;
import com.example.autokolcsonzes.Model.Rental;
import com.example.autokolcsonzes.Service.CarService;
import com.example.autokolcsonzes.Service.RentalSevice;
import com.example.autokolcsonzes.Utils.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RESTApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarService carService;

    @MockitoBean
    private RentalSevice rentalSevice;

    private Rental sampleRental;
    private Car sampleCar;

    @BeforeEach
    void setUp() {
        sampleRental = new Rental();
        sampleRental.setCarId(1);
        sampleRental.setStart("3000-01-01");
        sampleRental.setEnd("3000-01-05");
        sampleRental.setName("Teszt Janos");
        sampleRental.setEmail("test@gmail.com");
        sampleRental.setAddress("Szeged Tisza utca 68.");
        sampleRental.setPhone("+36301234567");

        sampleCar = new Car();
        sampleCar.setId(1);
        sampleCar.setName("Teszt Car");
        sampleCar.setPricePerDay(200);
        sampleCar.setActive(true);
    }


    @Test
    public void testSearch_ReturnsCars() throws Exception {
        Mockito.when(carService.searchCar(anyString(), anyString()))
                .thenReturn(List.of(sampleCar));

        mockMvc.perform(get("/rest/search")
                        .param("start", "3000-01-01")
                        .param("end", "3000-01-05"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(sampleCar.getId()))
                .andExpect(jsonPath("$[0].name").value(sampleCar.getName()));
    }

    @Test
    public void testSearch_NoCars_ReturnsNotFound() throws Exception {
        Mockito.when(carService.searchCar(anyString(), anyString()))
                .thenReturn(List.of());

        mockMvc.perform(get("/rest/search")
                        .param("start", "3000-01-01")
                        .param("end", "3000-01-05"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[0]").value("Sajnos a két időpont között nincs szabad autónk"));
    }


    @Test
    public void testSearch_ValidationException_ReturnsBadRequest() throws Exception {
        Mockito.when(carService.searchCar(anyString(), anyString()))
                .thenThrow(new ValidationException(List.of("Invalid date")));

        mockMvc.perform(get("/rest/search")
                        .param("start", "invalid")
                        .param("end", "invalid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Invalid date"));
    }


    @Test
    public void testAddRental_Success() throws Exception {
        Mockito.when(rentalSevice.createRental(any(Rental.class))).thenReturn(1);

        String json = """
                {
                    "carId": 1,
                    "start": "3000-01-01",
                    "end": "3000-01-05",
                    "name": "Teszt Janos",
                    "email": "test@gmail.com",
                    "address": "Szeged Tisza utca 68.",
                    "phone": "+36301234567"
                }
                """;

        mockMvc.perform(post("/rest/add")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("Az autó sikeresen lefoglalva"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testAddRental_ValidationException() throws Exception {
        Mockito.when(rentalSevice.createRental(any(Rental.class)))
                .thenThrow(new ValidationException(List.of("Invalid data")));

        String json = """
                {
                    "carId": 1,
                    "start": "invalid",
                    "end": "invalid",
                    "name": "Teszt Janos",
                    "email": "test@gmail.com",
                    "address": "Szeged Tisza utca 68.",
                    "phone": "+36301234567"
                }
                """;

        mockMvc.perform(post("/rest/add")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Invalid data"));
    }
}
