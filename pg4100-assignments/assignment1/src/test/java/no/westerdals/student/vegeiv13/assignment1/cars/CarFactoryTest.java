package no.westerdals.student.vegeiv13.assignment1.cars;

import no.westerdals.student.vegeiv13.assignment1.carrental.cars.CarFactory;
import no.westerdals.student.vegeiv13.assignment1.carrental.cars.CarFactoryException;
import no.westerdals.student.vegeiv13.assignment1.carrental.cars.RentalCar;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class CarFactoryTest {

    private CarFactory carFactory;

    @Before
    public void setup() {
        carFactory = new CarFactory("UF", 5);
    }

    @Test(expected = CarFactoryException.class)
    public void testBadPrefix() {
        new CarFactory("AAA", 5);
    }

    @Test(expected = CarFactoryException.class)
    public void testBadLength() {
        new CarFactory("AA", 1);
    }

    @Test
    public void testCreateCar() {
        RentalCar car = carFactory.createRentalCar();
        assertNotNull("Car is not null", car);
    }

    @Test
    public void testCreateMultipleCars() {
        int expectedSize = 20;
        List<RentalCar> cars = carFactory.createRentalCars(expectedSize);
        assertNotNull("We got a list of cars", cars);
        assertSame("The list of cars is large as expected", expectedSize, cars.size());
    }
}
