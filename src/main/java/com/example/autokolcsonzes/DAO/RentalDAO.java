package com.example.autokolcsonzes.DAO;

import com.example.autokolcsonzes.Model.Rental;

import java.util.List;

public interface RentalDAO {
    boolean createRental(Rental rental);
    List<Rental> listAll();
    List<Rental> listCurrent(String date);
    List<Rental> listCurrentForCar(int id,String date);
}
