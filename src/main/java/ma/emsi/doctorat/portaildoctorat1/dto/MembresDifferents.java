package ma.emsi.doctorat.portaildoctorat1.dto;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MembresDifferentsValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MembresDifferents {
    String message() default "Les membres du jury doivent être différents";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
