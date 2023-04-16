package com.vit_ana.scheduler.service.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.vit_ana.scheduler.model.UserForm;
import com.vit_ana.scheduler.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration-test")
public class UserServiceIT {

    @Autowired
    private UserService userService;

    @Test
    public void shouldSaveNewRetailCustomer() {
        UserForm userForm = UserFactoryTest.prepareSampleUserForm();

        userService.saveNewRetailCustomer(userForm);

        assertThat(userService.getAllRetailCustomers()).hasSize(2);
    }

}