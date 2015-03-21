package no.westerdals.student.vegeiv13.pg4100.assignment2;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Year;

@Converter(autoApply = true)
public class YearPersistenceConverter implements AttributeConverter<Year, Integer> {

    @Override
    public Integer convertToDatabaseColumn(final Year year) {
        return year.getValue();
    }

    @Override
    public Year convertToEntityAttribute(final Integer integer) {
        return Year.of(integer);
    }
}
