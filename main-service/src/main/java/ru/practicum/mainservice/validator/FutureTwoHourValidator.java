package ru.practicum.mainservice.validator;

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


        return localDate.isAfter(startDate) || localDate.isEqual(startDate);


    }
}