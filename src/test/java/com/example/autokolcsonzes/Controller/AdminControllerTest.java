package com.example.autokolcsonzes.Controller;

import com.example.autokolcsonzes.Model.Car;
import com.example.autokolcsonzes.Model.Rental;
import com.example.autokolcsonzes.Service.CarService;
import com.example.autokolcsonzes.Service.ImageService;
import com.example.autokolcsonzes.Service.RentalSevice;
import com.example.autokolcsonzes.Utils.CarDeactivationException;
import com.example.autokolcsonzes.Utils.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarService carService;

    @MockitoBean
    private RentalSevice rentalSevice;

    @MockitoBean
    private ImageService imageService;

    private Car sampleCar;

    @BeforeEach
    public void setUp() {
        sampleCar = new Car();
        sampleCar.setId(1);
        sampleCar.setName("Test Car");
        sampleCar.setPricePerDay(200);
        sampleCar.setActive(true);
    }

    @Test
    public void testGetCarsAddPage() throws Exception {
        mockMvc.perform(get("/admin/cars/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("/admin/adminCarsUpload"));
    }

    @Test
    public void testPostAddCarWithImage() throws Exception {
        Mockito.when(carService.addCar(any(Car.class))).thenReturn(1);
        Mockito.when(imageService.saveImage(anyInt(), any())).thenReturn(true);

        MockMultipartFile image = new MockMultipartFile("image", "car.jpg", "image/jpeg", "test".getBytes());

        mockMvc.perform(multipart("/admin/cars/add")
                        .file(image)
                        .param("name", "Test Car")
                        .param("pricePerDay", "200")
                        .param("active", "true")
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/cars/modify/1"))
                .andExpect(flash().attributeExists("success"));
    }

    @Test
    public void testGetCarModify() throws Exception {
        Mockito.when(carService.getCar(1)).thenReturn(sampleCar);

        mockMvc.perform(get("/admin/cars/modify/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/adminModifyCar"))
                .andExpect(model().attributeExists("car"));
    }

    @Test
    public void testPostModifyCarWithImage() throws Exception {
        Mockito.when(carService.modifyCar(any(Car.class))).thenReturn(true);
        Mockito.when(imageService.saveImage(anyInt(), any())).thenReturn(true);

        MockMultipartFile image = new MockMultipartFile("image", "car.jpg", "image/jpeg", "test".getBytes());

        mockMvc.perform(multipart("/admin/cars/modify/1")
                        .file(image)
                        .param("name", "Updated Car")
                        .param("pricePerDay", "250")
                        .param("active", "true")
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/cars/modify/1"))
                .andExpect(flash().attributeExists("success"));
    }

    @Test
    public void testPostModifyCar_DeactivationException() throws Exception {
        Mockito.doThrow(new CarDeactivationException(List.of(new Rental()))).when(carService).modifyCar(any(Car.class));
        MockMultipartFile image = new MockMultipartFile("image", "car.jpg", "image/jpeg", "test".getBytes());

        mockMvc.perform(multipart("/admin/cars/modify/1")
                        .file(image)
                        .param("name", "Updated Car")
                        .param("pricePerDay", "250")
                        .param("active", "true")
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/cars/modify/1"))
                .andExpect(flash().attributeExists("errors"));
    }
}
