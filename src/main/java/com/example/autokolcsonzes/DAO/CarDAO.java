package com.example.autokolcsonzes.DAO;

import com.example.autokolcsonzes.Model.Car;

import java.util.Date;
import java.util.List;

public interface CarDAO {
    List<Car> carSearchWithParam(String start,String end);
    List<Car> findAllCar();
    Car findCarById(int id);
    int createCar(Car car);
    boolean modifyCar(Car car);
    boolean isAvailable(int id,String start,String end);

}
