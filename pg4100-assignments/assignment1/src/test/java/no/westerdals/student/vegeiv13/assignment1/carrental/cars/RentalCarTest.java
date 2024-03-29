package no.westerdals.student.vegeiv13.assignment1.carrental.cars;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RentalCarTest {

    public static final String REGISTRATION_NUMBER = "AA12345";
    private Constructor<RentalCar> constructor;
    private int modifiers;
    private RentalCar rentalCar;

    @Before
    public void setup() throws Exception {
        constructor = RentalCar.class.getDeclaredConstructor(String.class);
        modifiers = constructor.getModifiers();
        constructor.setAccessible(true);
        rentalCar = constructor.newInstance(REGISTRATION_NUMBER);
    }

    @Test
    public void testConstructorIsProtected() throws Exception {
        assertTrue("Constructor is protected", Modifier.isProtected(modifiers));
    }

    @Test
    public void testNumberPlateProperty() {
        String expected = REGISTRATION_NUMBER;
        String actual = rentalCar.registrationNumberProperty().get();

        assertTrue("numberPlateProperty matches given value", expected.equals(actual));
    }

    @Test
    public void testEquals() throws Exception {
        RentalCar secondCar = constructor.newInstance("AA54321");

        assertFalse("The car does not match another car with different plates", rentalCar.equals(secondCar));
    }

    @Test
    public void testToString() {
        String actual = rentalCar.toString();
        String expected = "RentalCar{rentedBy=" + rentalCar.clientProperty() +
                ", registrationNumber='StringProperty [value: " +
                REGISTRATION_NUMBER + "]'}";
        assertTrue("ToString is as expected", expected.equals(actual));
    }

    @Test
    public void testHashCode() throws Exception {
        RentalCar secondCar = constructor.newInstance("AA54321");

        assertFalse("HashCodes differ across different instances with different values",
                rentalCar.hashCode() == secondCar.hashCode());
    }
}
