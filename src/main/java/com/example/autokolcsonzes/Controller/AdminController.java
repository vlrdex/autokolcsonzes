package com.example.autokolcsonzes.Controller;

import com.example.autokolcsonzes.Model.Car;
import com.example.autokolcsonzes.Service.CarService;
import com.example.autokolcsonzes.Service.ImageService;
import com.example.autokolcsonzes.Service.RentalSevice;
import com.example.autokolcsonzes.Utils.CarDeactivationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    public RentalSevice rentalSevice;
    @Autowired
    public CarService carService;
    @Autowired
    public ImageService imageService;



    @GetMapping("")
    public String admin(){
        return "admin/admin";
    }




    //admin rental control
    @GetMapping("/rentals/all")
    public String getAllRentals(Model model){
        model.addAttribute("rentals",rentalSevice.getAll());
        return "admin/adminRentals";
    }

    @GetMapping("/rentals/current")
    public String getCurrentRentals(Model model){
        model.addAttribute("rentals",rentalSevice.getCurrent());
        return "admin/adminRentals";
    }




    //admin car control
    @GetMapping("/cars")
    public String getCars(Model model){
        model.addAttribute("cars",carService.getCars());
        return "admin/adminCars";
    }


    @GetMapping("/cars/add")
    public String addCar(){
        return "/admin/adminCarsUpload";
    }


    @PostMapping("/cars/add")
    public String addWithImage(@RequestParam("name") String name,
                               @RequestParam("pricePerDay") int pricePerDay,
                               @RequestParam(name="active",defaultValue = "false") boolean active,
                               @RequestParam("image") MultipartFile image,
                               RedirectAttributes redirectAttributes)
    {
        List<String> success=new ArrayList<>();

        Car car=new Car();
        car.setName(name);
        car.setPricePerDay(pricePerDay);
        car.setActive(active);

        int id=carService.addCar(car);
        success.add("Autó sikeresen feltöltve");

        if(imageService.saveImage(id,image)){
            success.add("Kép sikeresen feltöltve");
        }

        redirectAttributes.addFlashAttribute("success",success);

        return "redirect:/admin/cars/modify/"+id;

    }



    @GetMapping("/cars/modify/{id}")
    public String getCar(@PathVariable("id") int id,Model model){
        Car car= carService.getCar(id);

        model.addAttribute("car",car);

        return "admin/adminModifyCar";
    }



    @PostMapping("/cars/modify/{id}")
    public String modifyCar(@PathVariable("id") int id,
                            @RequestParam("name") String name,
                            @RequestParam("pricePerDay") int pricePerDay,
                            @RequestParam(name="active",defaultValue = "false") boolean active,
                            @RequestParam("image") MultipartFile image,
                            RedirectAttributes redirectAttributes){
        List<String> success=new ArrayList<>();

        Car car=new Car();
        car.setId(id);
        car.setName(name);
        car.setPricePerDay(pricePerDay);
        car.setActive(active);

        try {
            carService.modifyCar(car);
            success.add("Autó sikeresen modosítva");
        }catch (CarDeactivationException ex){
            redirectAttributes.addFlashAttribute("errors",ex.getRentals());
            return "redirect:/admin/cars/modify/"+car.getId();
        }

        if(imageService.saveImage(id,image)){
            success.add("Kép sikeresen modositva");
        }

        redirectAttributes.addFlashAttribute("success",success);

        return "redirect:/admin/cars/modify/"+car.getId();


    }


}
