package no.westerdals.student.vegeiv13.pg4100.assignment2;

import org.junit.Before;
import org.junit.Test;

import java.time.Year;

import static org.junit.Assert.assertEquals;

public class YearPersistenceConverterTest {

    private YearPersistenceConverter converter;

    @Before
    public void setup() {
        converter = new YearPersistenceConverter();
    }

    @Test
    public void testConvertToDatabaseColumn() {
        int expected = 1999;
        Year year = Year.of(expected);
        int actual = converter.convertToDatabaseColumn(year);
        assertEquals("Converter returns the same int that we used to set up a year", expected, actual);
    }

    @Test
    public void testConvertToEntityAttribute() {
        Year expected = Year.of(2000);
        Year actual = converter.convertToEntityAttribute(2000);
        assertEquals("Converter returns a year equal to the value we passed", expected, actual);
    }
}
