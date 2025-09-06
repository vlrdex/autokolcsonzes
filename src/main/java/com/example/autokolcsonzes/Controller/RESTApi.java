package com.example.autokolcsonzes.Controller;

import com.example.autokolcsonzes.Config.WebSecurityConfig;
import com.example.autokolcsonzes.Model.Car;
import com.example.autokolcsonzes.Model.Rental;
import com.example.autokolcsonzes.Service.CarService;
import com.example.autokolcsonzes.Service.RentalSevice;
import com.example.autokolcsonzes.Utils.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest")
public class RESTApi {

    @Autowired
    private CarService carService;
    @Autowired
    private RentalSevice rentalSevice;

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam("start") String start,@RequestParam("end") String end){
        try {
            List<Car> cars = carService.searchCar(start, end);

            if (cars.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("errors", List.of("Sajnos a két időpont között nincs szabad autónk"));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            return ResponseEntity.ok(cars);

        } catch (ValidationException ex) {
            Map<String, Object> response = new HashMap<>();
            response.put("errors", ex.getErrors());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Rental rental){
        try {
            int generatedId=rentalSevice.createRental(rental);

            if(generatedId!=0){
                Map<String,Object> response = new HashMap<>();
                response.put("success","Az autó sikeresen lefoglalva");
                response.put("id", generatedId);
                return ResponseEntity.ok(response);
            }else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error","Valami hiba történt!"));
            }
        }catch (ValidationException ex){
            Map<String,Object> response=new HashMap<>();
            response.put("errors",ex.getErrors());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

}
