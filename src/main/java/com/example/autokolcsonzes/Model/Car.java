package com.example.autokolcsonzes.Model;


public class Car {
    private int id;
    private String name;
    private int pricePerDay;
    private boolean active;

    public Car(int id, String name, int pricePerDay, boolean active) {
        this.id = id;
        this.name = name;
        this.pricePerDay = pricePerDay;
        this.active = active;
    }

    public Car() {
        this.id = 0;
        this.name=null;
        this.pricePerDay=0;
        this.active=false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(int pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean equals(Car other){
        return this.id==other.id && this.name.equals(other.name) && this.pricePerDay==other.pricePerDay && this.active==other.active;
    }
}
