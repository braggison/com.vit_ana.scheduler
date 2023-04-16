package com.example.slabiak.appointmentscheduler.validation;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.vit_ana.scheduler.model.UserForm;
import com.vit_ana.scheduler.validation.groups.UpdateProvider;
import com.vit_ana.scheduler.validation.groups.UpdateUser;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class UpdateProviderValidationTest {

    private ValidatorFactory factory;
    private Validator validator;

    @Before
    public void stup() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldHave9ViolationsForEmptyFormWhenUpdateProvider() {
        UserForm form = new UserForm();
        Set<ConstraintViolation<UserForm>> violations = validator.validate(form, UpdateUser.class, UpdateProvider.class);
        assertEquals(violations.size(), 9);
    }
}
