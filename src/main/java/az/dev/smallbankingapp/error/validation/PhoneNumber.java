package az.dev.smallbankingapp.error.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Pattern(regexp = Patterns.PHONE_REGEX, message = ErrorMessages.INVALID_VALUE_FORMAT)
public @interface PhoneNumber {

    String message() default ErrorMessages.INVALID_VALUE_FORMAT;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}