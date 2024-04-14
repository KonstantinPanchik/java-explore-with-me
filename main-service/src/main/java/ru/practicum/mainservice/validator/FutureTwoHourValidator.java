package ru.practicum.mainservice.validator;

import ru.practicum.mainservice.exception.FutureTwoHourException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class FutureTwoHourValidator implements ConstraintValidator<FutureTwoHour, LocalDateTime> {
    @Override
    public boolean isValid(LocalDateTime localDate, ConstraintValidatorContext constraintValidatorContext) {

        if (localDate == null) {
            return true;
        }

        LocalDateTime startDate = LocalDateTime.now().plusHours(2);


        if (localDate.isAfter(startDate) || localDate.isEqual(startDate)) {
            return localDate.isAfter(startDate) || localDate.isEqual(startDate);
        } else {
            throw new FutureTwoHourException("Field: eventDate." +
                    " Error: должно содержать дату, которая еще не наступила. Value: " + localDate.toString());
        }

    }
}