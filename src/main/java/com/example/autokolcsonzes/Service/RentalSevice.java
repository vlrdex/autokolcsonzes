package com.example.autokolcsonzes.Service;

import com.example.autokolcsonzes.DAO.Impl.RentalDAOImpl;
import com.example.autokolcsonzes.DAO.RentalDAO;
import com.example.autokolcsonzes.Model.Rental;
import com.example.autokolcsonzes.Utils.ValidationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RentalSevice {
    RentalDAO rentalDAO;

    public RentalSevice(RentalDAOImpl rentalDAO){
        this.rentalDAO=rentalDAO;
    }

    public int createRental(Rental rental) throws ValidationException {
        if (rental==null){
            return 0;
        }

        List<String> errors=new ArrayList<>();

        try{
            LocalDate startDate=LocalDate.parse(rental.getStart());
            LocalDate endDate=LocalDate.parse(rental.getEnd());

            if(startDate.isBefore(getCurrentDate())){
                errors.add("A kivétel dátuma nem lehet a múltban.");
            }

            if (startDate.isAfter(endDate)){
                errors.add("„A visszahozás dátuma nem lehet korábbi, mint a kivétel dátuma.”");
            }
        }catch (Exception ex){
            errors.add("Az időpont formátuma nem megfelelő");
        }


        if (rental.getEmail() == null ||
                !rental.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            errors.add("Érvénytelen email cím.");
        }

        if (rental.getPhone() == null ||
                !rental.getPhone().matches("^[0-9+\\-\\s]{6,20}$")) {
            errors.add("Érvénytelen telefonszám.");
        }

        if (!errors.isEmpty()){
            throw new ValidationException(errors);
        }

        try {
            return rentalDAO.createRental(rental);
        }catch (RuntimeException ex){
            errors.add(ex.getMessage());
            errors.add("Ez az autó a megadott időszakban már foglalt!");
            throw new ValidationException(errors);
        }
    }

    public List<Rental> getAll(){
        return rentalDAO.listAll();
    }

    public List<Rental> getCurrent(){
        return rentalDAO.listCurrent(LocalDate.now().toString());
    }

    public List<Rental> getCurrentForCar(int id){
        return rentalDAO.listCurrentForCar(id,LocalDate.now().toString());
    }

    LocalDate getCurrentDate(){
        return LocalDate.now();
    }
}
