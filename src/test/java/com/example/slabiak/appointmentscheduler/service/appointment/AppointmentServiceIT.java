package com.example.slabiak.appointmentscheduler.service.appointment;

import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.entity.AppointmentStatus;
import com.example.slabiak.appointmentscheduler.service.AppointmentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration-test")
public class AppointmentServiceIT {

    @Autowired
    private AppointmentService appointmentService;

    @Test
    @Transactional
    @WithUserDetails("admin")
    public void shouldSaveNewRetailCustomer() {
        appointmentService.createNewAppointment(
        		UUID.fromString("0114860f-d6a0-4876-a88d-0b8e63512f78"), 
        		UUID.fromString("2625c480-8748-4d22-9fd2-db52cefaa6ed"), 
        		UUID.fromString("b9745f86-0433-4d15-ab6c-0281fded27c2"), 
        		LocalDateTime.of(2020, 02, 9, 12, 0, 0));

        List<Appointment> appointmentByProviderId = appointmentService.getAllAppointments();
        assertThat(appointmentByProviderId).hasSize(1);
        assertEquals(AppointmentStatus.SCHEDULED, appointmentByProviderId.get(0).getStatus());

    }

}
