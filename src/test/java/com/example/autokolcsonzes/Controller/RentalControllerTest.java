package com.example.autokolcsonzes.Controller;

import com.example.autokolcsonzes.Model.Rental;
import com.example.autokolcsonzes.Service.RentalSevice;
import com.example.autokolcsonzes.Utils.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@SpringBootTest
@AutoConfigureMockMvc
public class RentalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RentalSevice rentalSevice;

    private Rental sampleRental;

    @BeforeEach
    void setUp(){
        sampleRental=new Rental();
        sampleRental.setId(1);
        sampleRental.setCarId(1);
        sampleRental.setStart("3000-01-01");
        sampleRental.setEnd("3030-01-05");
        sampleRental.setName("Teszt Janos");
        sampleRental.setEmail("test@gmail.com");
        sampleRental.setAddress("Szeged Tissza utca 68.");
        sampleRental.setPhone("+36301234567");
    }


    @Test
    public void testAddWithGoodParam() throws Exception{
        Mockito.when(rentalSevice.createRental(any(Rental.class))).thenReturn(1);

        mockMvc.perform(post("/rentals/add").with(csrf())
                        .param("carid",String.valueOf(sampleRental.getCarId()))
                        .param("start",sampleRental.getStart())
                        .param("end",sampleRental.getEnd())
                        .param("name",sampleRental.getName())
                        .param("email",sampleRental.getEmail())
                        .param("address",sampleRental.getAddress())
                        .param("phone",sampleRental.getPhone())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("success"))
                .andExpect(model().attributeExists("admin"));
    }

    @Test
    public void testAddWithBadParam() throws Exception {
        Mockito.when(rentalSevice.createRental(any(Rental.class)))
                .thenThrow(new ValidationException(List.of("Test error")));

        mockMvc.perform(post("/rentals/add").with(csrf())
                        .param("carid", String.valueOf(sampleRental.getCarId()))
                        .param("start", sampleRental.getStart())
                        .param("end", sampleRental.getEnd())
                        .param("name", sampleRental.getName())
                        .param("email", sampleRental.getEmail())
                        .param("address", sampleRental.getAddress())
                        .param("phone", sampleRental.getPhone())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cars/" + sampleRental.getCarId()
                        + "?start=" + sampleRental.getStart()
                        + "&end=" + sampleRental.getEnd()))
                .andExpect(flash().attributeExists("errors"));
    }

    @Test
    public void testAddWithFailedCreation() throws Exception{
        Mockito.when(rentalSevice.createRental(any(Rental.class))).thenReturn(0);

        mockMvc.perform(post("/rentals/add").with(csrf())
                        .param("carid",String.valueOf(sampleRental.getCarId()))
                        .param("start",sampleRental.getStart())
                        .param("end",sampleRental.getEnd())
                        .param("name",sampleRental.getName())
                        .param("email",sampleRental.getEmail())
                        .param("address",sampleRental.getAddress())
                        .param("phone",sampleRental.getPhone())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("error"));

    }


}
