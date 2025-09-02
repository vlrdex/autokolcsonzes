package com.example.autokolcsonzes.Service;

import com.example.autokolcsonzes.DAO.CarDAO;
import com.example.autokolcsonzes.DAO.Impl.CarDAOImpl;
import com.example.autokolcsonzes.Model.Car;
import com.example.autokolcsonzes.Model.Rental;
import com.example.autokolcsonzes.Utils.CarDeactivationException;
import org.springframework.stereotype.Service;

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

    public List<Car> searchCar(String  start,String end){
        return carDAO.carSearchWithParam(start,end);
    }

    public Car getCar(int id){
        return carDAO.findCarById(id);
    }

    public List<Car> getCars(){
        return carDAO.findAllCar();
    }

    public boolean carIsAvailable(int id,String start,String end){
        return  carDAO.isAvailable(id,start,end);
    }

    public int addCar(Car car){
        return carDAO.createCar(car);
    }

    public boolean modifyCar(Car car) throws CarDeactivationException {
        List<Rental> rentals=rentalSevice.getCurrentForCar(car.getId());

        if (rentals.isEmpty()){
            return carDAO.modifyCar(car);
        }else {
            throw new CarDeactivationException(rentals);
        }
    }
}
