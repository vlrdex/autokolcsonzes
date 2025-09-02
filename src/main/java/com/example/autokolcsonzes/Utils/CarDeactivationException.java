package com.example.autokolcsonzes.Utils;

import com.example.autokolcsonzes.Model.Rental;

import java.util.List;

public class CarDeactivationException extends Exception{
    List<Rental> rentals;

    public CarDeactivationException(List<Rental> rentals){
        this.rentals=rentals;
    }

    public List<Rental> getRentals() {
        return rentals;
    }
}
