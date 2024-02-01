package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class AfterDateValidator implements ConstraintValidator<AfterDate, LocalDate> {

    private String dateParameter;

    @Override
    public void initialize(AfterDate constraintAnnotation) {
        this.dateParameter = constraintAnnotation.date();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        LocalDate specifiedDate = LocalDate.parse(dateParameter);
        return value.isAfter(specifiedDate);
    }
}
