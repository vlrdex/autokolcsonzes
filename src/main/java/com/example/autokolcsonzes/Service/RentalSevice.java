package com.example.autokolcsonzes.Service;

import com.example.autokolcsonzes.DAO.Impl.RentalDAOImpl;
import com.example.autokolcsonzes.DAO.RentalDAO;
import com.example.autokolcsonzes.Model.Rental;
import com.example.autokolcsonzes.Utils.RentalValidationException;
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

    public boolean createRental(Rental rental) throws RentalValidationException{
        List<String> errors=new ArrayList<>();

        LocalDate startDate=LocalDate.parse(rental.getStart());
        LocalDate endDate=LocalDate.parse(rental.getEnd());

        if(startDate.isBefore(LocalDate.now())){
            errors.add("A kivétel dátuma nem lehet a múltban.");
        }

        if (startDate.isAfter(endDate)){
            errors.add("„A visszahozás dátuma nem lehet korábbi, mint a kivétel dátuma.”");
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
            throw new RentalValidationException(errors);
        }

        try {
            return rentalDAO.createRental(rental);
        }catch (RuntimeException ex){
            errors.add("Ez az autó a megadott időszakban már foglalt!");
            throw new RentalValidationException(errors);
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
}
