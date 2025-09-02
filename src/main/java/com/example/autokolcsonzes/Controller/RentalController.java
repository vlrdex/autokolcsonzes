package com.example.autokolcsonzes.Controller;

import com.example.autokolcsonzes.Model.Rental;
import com.example.autokolcsonzes.Service.RentalSevice;
import com.example.autokolcsonzes.Utils.RentalValidationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class RentalController {
    private final RentalSevice rentalSevice;

    public RentalController(RentalSevice rentalSevice){
        this.rentalSevice=rentalSevice;
    }

    @PostMapping("/rentals/add")
    public String add(
            @RequestParam("carid") int carid,
            @RequestParam("start") String start,
            @RequestParam("end") String end,
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("address") String address,
            @RequestParam("phone") String phone,
            Model model,
            RedirectAttributes redirectAttributes
    ){
        Rental rental=new Rental();
        rental.setCarId(carid);
        rental.setStart(start);
        rental.setEnd(end);
        rental.setName(name);
        rental.setEmail(email);
        rental.setAddress(address);
        rental.setPhone(phone);

        try {
            if(rentalSevice.createRental(rental)){
                model.addAttribute("success","Az autó sikeresen lefoglalva");
                return "index";
            }else {
                return "error";
            }
        }catch (RentalValidationException ex){
            List<String> errors=ex.getErrors();
            System.out.println(errors);
            redirectAttributes.addFlashAttribute("errors",errors);
            return "redirect:/cars/"+carid+"?start="+start+"&end="+end;
        }


    }

}
