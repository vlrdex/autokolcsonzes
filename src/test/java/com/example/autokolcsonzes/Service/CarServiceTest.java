package com.example.autokolcsonzes.Service;

import com.example.autokolcsonzes.Model.Car;
import com.example.autokolcsonzes.Model.Rental;
import com.example.autokolcsonzes.Service.CarService;
import com.example.autokolcsonzes.Utils.CarDeactivationException;
import com.example.autokolcsonzes.Utils.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@SpringBootTest
@Transactional
public class CarServiceTest {

    @Autowired
    private CarService carService;
    @Autowired
    private RentalSevice rentalSevice;

    @Test
    public void testAddCarAndGetCarWorks(){
        Car tesztCar=new Car();
        tesztCar.setName("teszt");
        tesztCar.setPricePerDay(2000);
        tesztCar.setActive(true);
        tesztCar.setId(carService.addCar(tesztCar));

        Car tesztCar2=carService.getCar(tesztCar.getId());

        Assertions.assertTrue(tesztCar.equals(tesztCar2));
    }

    @Test
    public void testAddCarWithNullParam(){
        Assertions.assertEquals(0,carService.addCar(null));
    }

    @Test
    public void testGetCarWithNotExistingId(){
        Assertions.assertNull(carService.getCar(1));
    }

    @Test
    public void testModifyCarWorks() throws CarDeactivationException {
        Car testCar = new Car();
        testCar.setName("teszt");
        testCar.setPricePerDay(200);
        testCar.setActive(true);

        testCar.setId(carService.addCar(testCar));

        testCar.setName("teszt2");
        testCar.setPricePerDay(20);
        testCar.setActive(false);

        //nem tud dobmi exapction itt nem kell kezelni
        if(carService.modifyCar(testCar)){
            Car testCar2=carService.getCar(testCar.getId());
            Assertions.assertTrue(testCar.equals(testCar2));
        }
    }

    @Test
    public void testModifyCarWithRentalCarStaysActice() throws CarDeactivationException {
        Car testCar = new Car();
        testCar.setName("teszt");
        testCar.setPricePerDay(200);
        testCar.setActive(true);

        testCar.setId(carService.addCar(testCar));

        Rental rental=new Rental();
        rental.setCarId(testCar.getId());
        rental.setStart("3000-01-01");
        rental.setEnd("3000-02-01");
        rental.setName("Teszt Jánós");
        rental.setEmail("test@gmail.com");
        rental.setAddress("Szeged Tisza utca 68.");
        rental.setPhone("+36301234567");

        rental.setId(rentalSevice.createRental(rental));

        testCar.setName("teszt2");
        testCar.setPricePerDay(2000);

        //nem szabad hibát dobnia
        if (carService.modifyCar(testCar)){
            Car teszt2=carService.getCar(testCar.getId());
            Assertions.assertTrue(testCar.equals(teszt2));
        }
    }

    @Test
    public void testModifyCarWithRentalCarBecomesInactice(){
        Car testCar = new Car();
        testCar.setName("teszt");
        testCar.setPricePerDay(200);
        testCar.setActive(true);

        testCar.setId(carService.addCar(testCar));

        Rental rental=new Rental();
        rental.setCarId(testCar.getId());
        rental.setStart("3000-01-01");
        rental.setEnd("3000-02-01");
        rental.setName("Teszt Jánós");
        rental.setEmail("test@gmail.com");
        rental.setAddress("Szeged Tisza utca 68.");
        rental.setPhone("+36301234567");

        rental.setId(rentalSevice.createRental(rental));

        testCar.setName("teszt2");
        testCar.setPricePerDay(2000);
        testCar.setActive(false);

       Assertions.assertThrows(CarDeactivationException.class,() -> carService.modifyCar(testCar));
    }

    @Test
    public void testModifyCarWithNullParam() throws Exception{
        Assertions.assertFalse(carService.modifyCar(null));
    }

    @Test
    public void testSearchWithGoodParams(){
        //hozzá adok 3 kocsit (2 aktiv 1 nem) és vissza várom 2

        Car teszt1=new Car();
        teszt1.setName("teszt1");
        teszt1.setPricePerDay(200);
        teszt1.setActive(true);

        Car teszt2=new Car();
        teszt2.setActive(true);
        teszt2.setName("teszt2");
        teszt2.setPricePerDay(300);

        Car teszt3=new Car();
        teszt3.setActive(false);
        teszt3.setName("teszt3");
        teszt3.setPricePerDay(300);

        teszt1.setId(carService.addCar(teszt1));
        teszt2.setId(carService.addCar(teszt2));
        teszt3.setId(carService.addCar(teszt3));

        List<Car> cars = carService.searchCar("3000-01-01","3000-02-01");

        //db-ből mindig id szerinti sorendbe jönnek vissza azért elég igy tesztelni
        Assertions.assertTrue(cars.size()==2 && cars.get(0).equals(teszt1) && cars.get(1).equals(teszt2));

    }

    @Test
    public void testSearchWithOneNullParam(){
        Assertions.assertTrue(carService.searchCar(null,"2024-01-09").isEmpty() && carService.searchCar("2024-01-09",null).isEmpty());
    }

    @Test
    public void testSearchWithTwoNullParam(){
        Assertions.assertTrue(carService.searchCar(null,null).isEmpty());
    }

    @Test
    public void testSearchWithLaterStartThenEndParam(){
        Assertions.assertThrows(ValidationException.class,() -> carService.searchCar("3000-02-01","3000-01-01"));
    }

    @Test
    public void testSearchWithStartTimeFromPast(){
        Assertions.assertThrows(ValidationException.class,() -> carService.searchCar("0300-01-01","0300-02-01"));
    }

    @Test
    public void  testSearchWithFrongTimeFormat(){
        Assertions.assertThrows(ValidationException.class,() -> carService.searchCar("alma","banán"));
    }

}
