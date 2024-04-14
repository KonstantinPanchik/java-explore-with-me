package ru.practicum.mainservice.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FutureTwoHourValidator.class)
@Documented
public @interface FutureTwoHour {


    String message() default "Event has to be in two hours after now";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


