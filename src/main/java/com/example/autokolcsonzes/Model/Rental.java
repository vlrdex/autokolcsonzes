package com.example.autokolcsonzes.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class Rental {
    private int id;
    private int carId;
    private String start;
    private String end;
    private String name;
    private String email;
    private String address;
    private String phone;

    public Rental(int id, int carId, String start, String end, String name, String email, String address, String phone) {
        this.id = id;
        this.carId = carId;
        this.start = start;
        this.end = end;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

    public Rental(){
        this.id = 0;
        this.carId = 0;
        this.start = null;
        this.end = null;
        this.name = null;
        this.email = null;
        this.address = null;
        this.phone = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
