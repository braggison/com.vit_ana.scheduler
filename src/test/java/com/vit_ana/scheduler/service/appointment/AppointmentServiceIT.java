package com.vit_ana.scheduler.service.appointment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.vit_ana.scheduler.dao.WorkingPlanRepository;
import com.vit_ana.scheduler.entity.Appointment;
import com.vit_ana.scheduler.entity.AppointmentStatus;
import com.vit_ana.scheduler.entity.WorkingPlan;
import com.vit_ana.scheduler.service.AppointmentService;
import com.vit_ana.scheduler.service.WorkingPlanService;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.TimeZone;
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
        int appointmentsCount = appointmentService.getAllAppointments().size();
        appointmentService.createNewAppointment(
        		UUID.fromString("0114860f-d6a0-4876-a88d-0b8e63512f78"), 
        		UUID.fromString("629f43d1-e89f-471e-ba0f-61bdbdb359d0"), 
        		UUID.fromString("4d003570-cd94-4fdb-a5cf-667ca8b98fa2"), 
        		OffsetDateTime.of(2020, 02, 9, 12, 0, 0, 0, ZoneOffset.ofTotalSeconds(TimeZone.getDefault().getRawOffset()/1000)));

        List<Appointment> appointmentByProviderId = appointmentService.getAllAppointments();
        assertThat(appointmentByProviderId).hasSize(++appointmentsCount);
        assertEquals(AppointmentStatus.SCHEDULED, appointmentByProviderId.get(0).getStatus());

    }
}
