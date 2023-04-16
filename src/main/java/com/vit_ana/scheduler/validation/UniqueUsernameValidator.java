package com.vit_ana.scheduler.validation;

import org.springframework.beans.factory.annotation.Autowired;

import com.vit_ana.scheduler.service.UserService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, Object> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(final UniqueUsername constraintAnnotation) {
    }

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        String userName = (String) obj;
        return !userService.userExists(userName);
    }

}