package com.example.autokolcsonzes.Service;

import com.example.autokolcsonzes.Model.Car;
import com.example.autokolcsonzes.Model.Rental;
import com.example.autokolcsonzes.Utils.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Transactional
public class RentalServiceTest {

    @Autowired
    private CarService carService;

    @Autowired
    private RentalSevice rentalSevice;

    @Test
    public void testCreateRentalAndGetAllRentalWorks(){
        Car car=new Car();
        car.setName("teszt");
        car.setPricePerDay(200);
        car.setActive(true);

        car.setId(carService.addCar(car));



        Rental rental=new Rental();
        rental.setCarId(car.getId());
        rental.setStart("3000-01-01");
        rental.setEnd("3000-02-01");
        rental.setName("Teszt János");
        rental.setEmail("teszt@gmail.com");
        rental.setPhone("+36306566297");
        rental.setAddress("Szeged Tisza utca 63.");

        rental.setId(rentalSevice.createRental(rental));

        Rental rental1=rentalSevice.getAll().get(0);

        Assertions.assertEquals(rental, rental1);
    }

    @Test
    public void testGetAllWorksWithMultipleRentals(){
        Car car=new Car();
        car.setName("teszt");
        car.setPricePerDay(200);
        car.setActive(true);

        car.setId(carService.addCar(car));



        Rental rental=new Rental();
        rental.setCarId(car.getId());
        rental.setStart("3000-02-02");
        rental.setEnd("3000-03-01");
        rental.setName("Teszt János");
        rental.setEmail("teszt@gmail.com");
        rental.setPhone("+36306566297");
        rental.setAddress("Szeged Tisza utca 63.");

        rental.setId(rentalSevice.createRental(rental));

        Rental rental1=new Rental();
        rental1.setCarId(car.getId());
        rental1.setStart("3000-01-01");
        rental1.setEnd("3000-02-01");
        rental1.setName("Teszt Béla");
        rental1.setEmail("teszt2@gmail.com");
        rental1.setPhone("+36301234567");
        rental1.setAddress("Szeged Tisza utca 66.");

        rental1.setId(rentalSevice.createRental(rental1));

        List<Rental> rentals = rentalSevice.getAll();

        //a db index szerinti sorendben dobja vissza
        Assertions.assertTrue(rentals.size() ==2 && rentals.get(0).equals(rental) && rentals.get(1).equals(rental1));


    }

    @Test
    public void testGetCurrentWorksWithMultipleRentals(){
        Car car=new Car();
        car.setName("teszt");
        car.setPricePerDay(200);
        car.setActive(true);

        car.setId(carService.addCar(car));

        RentalSevice spyService = Mockito.spy(rentalSevice);
        Mockito.doReturn(LocalDate.of(100,01,01)).when(spyService).getCurrentDate();


        Rental rental=new Rental();
        rental.setCarId(car.getId());
        rental.setStart("3000-02-02");
        rental.setEnd("3000-03-01");
        rental.setName("Teszt János");
        rental.setEmail("teszt@gmail.com");
        rental.setPhone("+36306566297");
        rental.setAddress("Szeged Tisza utca 63.");

        rental.setId(rentalSevice.createRental(rental));

        Rental rental1=new Rental();
        rental1.setCarId(car.getId());
        rental1.setStart("3000-01-01");
        rental1.setEnd("3000-02-01");
        rental1.setName("Teszt Béla");
        rental1.setEmail("teszt2@gmail.com");
        rental1.setPhone("+36301234567");
        rental1.setAddress("Szeged Tisza utca 66.");

        rental1.setId(rentalSevice.createRental(rental1));

        Rental rental2=new Rental();
        rental2.setCarId(car.getId());
        rental2.setStart("0300-01-01");
        rental2.setEnd("0300-02-01");
        rental2.setName("Teszt Elek");
        rental2.setEmail("teszt3@gmail.com");
        rental2.setPhone("+36306936985");
        rental2.setAddress("Szeged Tisza utca 69.");

        rental2.setId(spyService.createRental(rental2));

        List<Rental> rentals = rentalSevice.getCurrent();

        //a db index szerinti sorendben dobja vissza
        Assertions.assertTrue(rentals.size() ==2 && rentals.get(0).equals(rental) && rentals.get(1).equals(rental1));
    }

    @Test
    public void testCreateRentalWithDateFromPast(){
        Car car=new Car();
        car.setName("teszt");
        car.setPricePerDay(200);
        car.setActive(true);

        Rental rental2=new Rental();
        rental2.setCarId(car.getId());
        rental2.setStart("0300-01-01");
        rental2.setEnd("0300-02-01");
        rental2.setName("Teszt Elek");
        rental2.setEmail("teszt3@gmail.com");
        rental2.setPhone("+36306936985");
        rental2.setAddress("Szeged Tisza utca 69.");

        Assertions.assertThrows(ValidationException.class,()->rentalSevice.createRental(rental2));
    }

    @Test
    public void testCreateRentalWithStartDateBeforEnd(){
        Car car=new Car();
        car.setName("teszt");
        car.setPricePerDay(200);
        car.setActive(true);

        Rental rental2=new Rental();
        rental2.setCarId(car.getId());
        rental2.setStart("3000-02-01");
        rental2.setEnd("3000-01-01");
        rental2.setName("Teszt Elek");
        rental2.setEmail("teszt3@gmail.com");
        rental2.setPhone("+36306936985");
        rental2.setAddress("Szeged Tisza utca 69.");

        Assertions.assertThrows(ValidationException.class,()->rentalSevice.createRental(rental2));
    }

    @Test
    public void testCreateRentalWithInvalidEmail(){
        Car car=new Car();
        car.setName("teszt");
        car.setPricePerDay(200);
        car.setActive(true);

        Rental rental2=new Rental();
        rental2.setCarId(car.getId());
        rental2.setStart("3000-02-01");
        rental2.setEnd("3000-03-01");
        rental2.setName("Teszt Elek");
        rental2.setEmail("teszt3!gmail.com");
        rental2.setPhone("+36306936985");
        rental2.setAddress("Szeged Tisza utca 69.");

        Assertions.assertThrows(ValidationException.class,()->rentalSevice.createRental(rental2));
    }

    @Test
    public void testCreateRentalWithInvalidPhone(){
        Car car=new Car();
        car.setName("teszt");
        car.setPricePerDay(200);
        car.setActive(true);

        Rental rental2=new Rental();
        rental2.setCarId(car.getId());
        rental2.setStart("3000-01-01");
        rental2.setEnd("3000-02-01");
        rental2.setName("Teszt Elek");
        rental2.setEmail("teszt3!gmail.com");
        rental2.setPhone("+36306936985");
        rental2.setAddress("Szeged Tisza utca 69.");

        Assertions.assertThrows(ValidationException.class,()->rentalSevice.createRental(rental2));
    }

    @Test
    public void testCreateRentalWithNullStartDate(){
        Car car=new Car();
        car.setName("teszt");
        car.setPricePerDay(200);
        car.setActive(true);

        Rental rental2=new Rental();
        rental2.setCarId(car.getId());
        rental2.setStart(null);
        rental2.setEnd("3000-01-01");
        rental2.setName("Teszt Elek");
        rental2.setEmail("teszt3!gmail.com");
        rental2.setPhone("+36306936985");
        rental2.setAddress("Szeged Tisza utca 69.");

        Assertions.assertThrows(ValidationException.class,()->rentalSevice.createRental(rental2));
    }

    @Test
    public void testCreateRentalWithNullEndDate(){
        Car car=new Car();
        car.setName("teszt");
        car.setPricePerDay(200);
        car.setActive(true);

        Rental rental2=new Rental();
        rental2.setCarId(car.getId());
        rental2.setStart("3000-02-01");
        rental2.setEnd(null);
        rental2.setName("Teszt Elek");
        rental2.setEmail("teszt3!gmail.com");
        rental2.setPhone("+36306936985");
        rental2.setAddress("Szeged Tisza utca 69.");

        Assertions.assertThrows(ValidationException.class,()->rentalSevice.createRental(rental2));
    }

    @Test
    public void testCreateRentalWithMultipleNull(){
        Car car=new Car();
        car.setName("teszt");
        car.setPricePerDay(200);
        car.setActive(true);


        Rental rental1=new Rental();
        rental1.setCarId(car.getId());
        rental1.setStart(null);
        rental1.setEnd(null);
        rental1.setName("Teszt Béla");
        rental1.setEmail(null);
        rental1.setPhone("+36301234567");
        rental1.setAddress("Szeged Tisza utca 66.");


        Assertions.assertThrows(ValidationException.class,()->rentalSevice.createRental(rental1));
    }


}
