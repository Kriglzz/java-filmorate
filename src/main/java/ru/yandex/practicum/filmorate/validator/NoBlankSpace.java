package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NoBlankSpaceValidator.class)
public @interface NoBlankSpace {
    String message() default "Поле не должно содержать пробелов";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
