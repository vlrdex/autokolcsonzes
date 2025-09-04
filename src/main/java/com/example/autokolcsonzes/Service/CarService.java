package com.example.autokolcsonzes.Service;

import com.example.autokolcsonzes.DAO.CarDAO;
import com.example.autokolcsonzes.DAO.Impl.CarDAOImpl;
import com.example.autokolcsonzes.Model.Car;
import com.example.autokolcsonzes.Model.Rental;
import com.example.autokolcsonzes.Utils.CarDeactivationException;
import com.example.autokolcsonzes.Utils.ValidationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CarService {
    private final CarDAO carDAO;
    private final RentalSevice rentalSevice;

    public CarService(CarDAOImpl carDAO,RentalSevice rentalSevice){
        this.carDAO=carDAO;
        this.rentalSevice=rentalSevice;
    }

    public List<Car> searchCar(String start,String end){

        List<String> errors = new ArrayList<>();

        if (start==null || end==null || start.isBlank() || end.isBlank()){
            return new ArrayList<>();
        }

        try {
            LocalDate startDate=LocalDate.parse(start);
            LocalDate endDate=LocalDate.parse(end);

            if(startDate.isBefore(getCurrentDate())){
                errors.add("A kivétel dátuma nem lehet a múltban.");
            }

            if (startDate.isAfter(endDate)){
                errors.add("A visszahozás dátuma nem lehet korábbi, mint a kivétel dátuma.");
            }

        }catch (Exception ex){
            errors.add("Hibás időpont formátum");
            throw new ValidationException(errors);
        }



        if (errors.isEmpty()){
            return carDAO.carSearchWithParam(start,end);
        }else {
            throw new  ValidationException(errors);
        }

    }

    public Car getCar(int id){
        try {
            return carDAO.findCarById(id);
        }catch (Exception ex){
            return null;
        }
    }

    public List<Car> getCars(){
        return carDAO.findAllCar();
    }

    public boolean carIsAvailable(int id,String start,String end){
        return  carDAO.isAvailable(id,start,end);
    }

    public int addCar(Car car){
        if (car==null){
            return 0;
        }
        return carDAO.createCar(car);
    }

    public boolean modifyCar(Car car) throws CarDeactivationException {
        if (car==null){
            return false;
        }

        List<Rental> rentals=rentalSevice.getCurrentForCar(car.getId());

        if (rentals.isEmpty() || car.isActive()){
            return carDAO.modifyCar(car);
        }else {
            throw new CarDeactivationException(rentals);
        }
    }


    LocalDate getCurrentDate(){
        return LocalDate.now();
    }
}
