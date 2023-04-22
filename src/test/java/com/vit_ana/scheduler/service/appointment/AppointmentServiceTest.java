package com.vit_ana.scheduler.service.appointment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.vit_ana.scheduler.dao.AppointmentRepository;
import com.vit_ana.scheduler.entity.Appointment;
import com.vit_ana.scheduler.entity.Work;
import com.vit_ana.scheduler.entity.WorkingPlan;
import com.vit_ana.scheduler.entity.user.customer.Customer;
import com.vit_ana.scheduler.entity.user.provider.Provider;
import com.vit_ana.scheduler.service.EmailService;
import com.vit_ana.scheduler.service.NotificationService;
import com.vit_ana.scheduler.service.UserService;
import com.vit_ana.scheduler.service.WorkService;
import com.vit_ana.scheduler.service.impl.AppointmentServiceImpl;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private WorkService workService;

    @Mock
    private EmailService emailService;

    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;


    @InjectMocks
    private AppointmentServiceImpl appointmentService;


    private UUID customerId;
    private UUID providerId;
    private UUID workId;

    private Appointment appointment;
    private Optional<Appointment> optionalAppointment;
    private List<Appointment> appointments;
    private UUID appointmentId;
    private Work work;
    private Provider provider;
    private Customer customer;

    @Before
    public void initObjects() {

        customerId = UUID.randomUUID();
        providerId = UUID.randomUUID();
        workId = UUID.randomUUID();
        work = new Work();
        work.setId(workId);
        work.setDuration(60);
        provider = new Provider();
        provider.setId(providerId);
        provider.setWorkingPlan(WorkingPlan.generateDefaultWorkingPlan());
        customer = new Customer();
        customer.setId(customerId);
        appointment = new Appointment();
        appointmentId = UUID.randomUUID();
        appointment.setId(appointmentId);
        optionalAppointment = Optional.of(appointment);
        appointments = new ArrayList<>();
        appointments.add(appointment);

    }

    @Test
    public void shouldBookAppointmentWhenAllConditionsMet() {
        OffsetDateTime startOfNewAppointment = OffsetDateTime.of(2019, 01, 01, 6, 0, 0, 0, ZoneOffset.ofTotalSeconds(TimeZone.getDefault().getRawOffset()/1000));

        when(workService.isWorkForCustomer(workId, customerId)).thenReturn(true);
        when(workService.getWorkById(workId)).thenReturn(work);
        when(userService.getProviderById(providerId)).thenReturn(provider);
        when(userService.getCustomerById(customerId)).thenReturn(customer);

        ArgumentCaptor<Appointment> argumentCaptor = ArgumentCaptor.forClass(Appointment.class);
        appointmentService.createNewAppointment(workId, providerId, customerId, startOfNewAppointment);

        verify(appointmentRepository, times(1)).save(argumentCaptor.capture());
    }

    @Test(expected = RuntimeException.class)
    public void shouldNotBookAppointmentWhenAppointmentStartIsNotWithinProviderWorkingHours() {
        OffsetDateTime startOfNewAppointment = OffsetDateTime.of(2019, 01, 01, 5, 59, 0, 0, ZoneOffset.ofTotalSeconds(TimeZone.getDefault().getRawOffset()/1000));

        when(workService.isWorkForCustomer(workId, customerId)).thenReturn(true);
        when(workService.getWorkById(workId)).thenReturn(work);
        when(userService.getProviderById(providerId)).thenReturn(provider);

        ArgumentCaptor<Appointment> argumentCaptor = ArgumentCaptor.forClass(Appointment.class);
        appointmentService.createNewAppointment(workId, providerId, customerId, startOfNewAppointment);
        verify(appointmentRepository, times(1)).save(argumentCaptor.capture());
    }

    @Test(expected = RuntimeException.class)
    public void shouldNotBookNewAppointmentWhenCollidingWithProviderAlreadyBookedAppointments() {
        OffsetDateTime startOfNewAppointment = OffsetDateTime.of(2019, 01, 01, 6, 0, 0, 0, ZoneOffset.ofTotalSeconds(TimeZone.getDefault().getRawOffset()/1000));

        Appointment existingAppointment = new Appointment();
        OffsetDateTime startOfExistingAppointment = OffsetDateTime.of(2019, 01, 01, 6, 0, 0, 0, ZoneOffset.ofTotalSeconds(TimeZone.getDefault().getRawOffset()/1000));
        OffsetDateTime endOfExistingAppointment = OffsetDateTime.of(2019, 01, 01, 7, 0, 0, 0, ZoneOffset.ofTotalSeconds(TimeZone.getDefault().getRawOffset()/1000));
        existingAppointment.setStart(startOfExistingAppointment);
        existingAppointment.setEnd(endOfExistingAppointment);
        List<Appointment> providerBookedAppointments = new ArrayList<>();
        providerBookedAppointments.add(existingAppointment);

        when(workService.isWorkForCustomer(workId, customerId)).thenReturn(true);
        when(appointmentRepository.findByProviderIdWithStartInPeriod(providerId, startOfNewAppointment, startOfNewAppointment.plusDays(1))).thenReturn(providerBookedAppointments);
        when(workService.getWorkById(workId)).thenReturn(work);
        when(userService.getProviderById(providerId)).thenReturn(provider);

        ArgumentCaptor<Appointment> argumentCaptor = ArgumentCaptor.forClass(Appointment.class);
        appointmentService.createNewAppointment(workId, providerId, customerId, startOfNewAppointment);

        verify(appointmentRepository, times(1)).save(argumentCaptor.capture());
    }

    @Test(expected = RuntimeException.class)
    public void shouldNotBookNewAppointmentWhenCollidingWithCustomerAlreadyBookedAppointments() {
        OffsetDateTime startOfNewAppointment = OffsetDateTime.of(2019, 01, 01, 6, 0, 0, 0, ZoneOffset.ofTotalSeconds(TimeZone.getDefault().getRawOffset()/1000));

        Appointment existingAppointment = new Appointment();
        OffsetDateTime startOfExistingAppointment = OffsetDateTime.of(2019, 01, 01, 6, 0, 0, 0, ZoneOffset.ofTotalSeconds(TimeZone.getDefault().getRawOffset()/1000));
        OffsetDateTime endOfExistingAppointment = OffsetDateTime.of(2019, 01, 01, 7, 0, 0, 0, ZoneOffset.ofTotalSeconds(TimeZone.getDefault().getRawOffset()/1000));
        existingAppointment.setStart(startOfExistingAppointment);
        existingAppointment.setEnd(endOfExistingAppointment);
        List<Appointment> customerBookedAppointments = new ArrayList<>();
        customerBookedAppointments.add(existingAppointment);

        when(workService.isWorkForCustomer(workId, customerId)).thenReturn(true);
        when(appointmentRepository.findByCustomerIdWithStartInPeriod(customerId, startOfNewAppointment, startOfNewAppointment.plusDays(1))).thenReturn(customerBookedAppointments);
        when(workService.getWorkById(workId)).thenReturn(work);
        when(userService.getProviderById(providerId)).thenReturn(provider);

        ArgumentCaptor<Appointment> argumentCaptor = ArgumentCaptor.forClass(Appointment.class);
        appointmentService.createNewAppointment(workId, providerId, customerId, startOfNewAppointment);

        verify(appointmentRepository, times(1)).save(argumentCaptor.capture());
    }


    @Test
    public void shouldFindAppointmentById() {
        when(appointmentRepository.findById(appointmentId)).thenReturn(optionalAppointment);
        assertEquals(optionalAppointment.get().getId(), appointmentService.getAppointmentByIdWithAuthorization(appointmentId).getId());
        verify(appointmentRepository, times(1)).findById(appointmentId);
    }

    @Test
    public void shouldFindAllAppointments() {
        when(appointmentRepository.findAll()).thenReturn(appointments);
        assertEquals(appointments, appointmentService.getAllAppointments());
        verify(appointmentRepository).findAll();
    }

    @Test
    public void shouldDeleteAppointmentById() {
        appointmentService.deleteAppointmentById(appointmentId);
        verify(appointmentRepository).deleteById(appointmentId);
    }


}
