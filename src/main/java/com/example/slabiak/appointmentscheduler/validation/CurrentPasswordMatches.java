package com.example.slabiak.appointmentscheduler.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CurrentPasswordMatchesValidator.class)
public @interface CurrentPasswordMatches {

    String message() default "Wrong current password";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
