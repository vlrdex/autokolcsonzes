package com.example.autokolcsonzes.Controller;

import com.example.autokolcsonzes.Config.WebSecurityConfig;
import com.example.autokolcsonzes.Model.Car;
import com.example.autokolcsonzes.Service.CarService;
import com.example.autokolcsonzes.Service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CarController {
    @Autowired
    private CarService carService;

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("admin", WebSecurityConfig.getIsAdmin());

        return "index";
    }

    @GetMapping("/cars/search")
    public String search(@RequestParam("start") String start, @RequestParam("end") String end, Model model){

        List<String> errors = new ArrayList<>();

        LocalDate startDate=LocalDate.parse(start);
        LocalDate endDate=LocalDate.parse(end);

        if(startDate.isBefore(LocalDate.now())){
            errors.add("A kivétel dátuma nem lehet a múltban.");
        }

        if (startDate.isAfter(endDate)){
            errors.add("„A visszahozás dátuma nem lehet korábbi, mint a kivétel dátuma.”");
        }

        List<Car> cars=carService.searchCar(start,end);

        if (cars.isEmpty()){
            errors.add("Sajnos a két időpont között nincs szabad autónk");
        }

        if (errors.isEmpty()){
            model.addAttribute("cars",cars);
            return "searchResult";
        }else {
            model.addAttribute("errors",errors);
            return "index";
        }
    }

    @GetMapping("/cars/{id}")
    public String getCar(@PathVariable("id") String idString,
                         @RequestParam("start") String start,
                         @RequestParam("end") String end,
                         Model model)
    {

        try {
            int id=Integer.parseInt(idString);

            Car car=carService.getCar(id);
            model.addAttribute("car",car);

            LocalDate startDate=LocalDate.parse(start);
            LocalDate endDate=LocalDate.parse(end);

            model.addAttribute("days", ChronoUnit.DAYS.between(startDate,endDate)+1);

            return "carRental";

        }catch (NumberFormatException e){
            return "error";
        }
    }




}
