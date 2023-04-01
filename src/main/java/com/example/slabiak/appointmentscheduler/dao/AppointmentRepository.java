package com.example.slabiak.appointmentscheduler.dao;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.slabiak.appointmentscheduler.entity.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    @Query("select a from Appointment a where a.customer.id = :customerId")
    List<Appointment> findByCustomerId(@Param("customerId") UUID customerId);

    @Query("select a from Appointment a where a.provider.id = :providerId")
    List<Appointment> findByProviderId(@Param("providerId") UUID providerId);

    @Query("select a from Appointment a where a.canceler.id = :userId")
    List<Appointment> findCanceledByUser(@Param("userId") UUID userId);

    @Query("select a from Appointment a where  a.status='SCHEDULED' and (a.customer.id = :userId or a.provider.id = :userId)")
    List<Appointment> findScheduledByUserId(@Param("userId") UUID userId);

    @Query("select a from Appointment a where a.provider.id = :providerId and  a.start >=:dayStart and  a.start <=:dayEnd")
    List<Appointment> findByProviderIdWithStartInPeroid(@Param("providerId") UUID providerId, @Param("dayStart") OffsetDateTime startPeroid, @Param("dayEnd") OffsetDateTime endPeroid);

    @Query("select a from Appointment a where a.customer.id = :customerId and  a.start >=:dayStart and  a.start <=:dayEnd")
    List<Appointment> findByCustomerIdWithStartInPeroid(@Param("customerId") UUID customerId, @Param("dayStart") OffsetDateTime startPeroid, @Param("dayEnd") OffsetDateTime endPeroid);

    @Query("select a from Appointment a where a.customer.id = :customerId and a.canceler.id =:customerId and a.canceledAt >=:date")
    List<Appointment> findByCustomerIdCanceledAfterDate(@Param("customerId") UUID customerId, @Param("date") OffsetDateTime date);

    @Query("select a from Appointment a where a.status = 'SCHEDULED' and :now >= a.end")
    List<Appointment> findScheduledWithEndBeforeDate(@Param("now") OffsetDateTime now);

    @Query("select a from Appointment a where a.status = 'SCHEDULED' and :date >= a.end and (a.customer.id = :userId or a.provider.id = :userId)")
    List<Appointment> findScheduledByUserIdWithEndBeforeDate(@Param("date") OffsetDateTime date, @Param("userId") UUID userId);

    @Query("select a from Appointment a where a.status = 'FINISHED' and :date >= a.end")
    List<Appointment> findFinishedWithEndBeforeDate(@Param("date") OffsetDateTime date);

    @Query("select a from Appointment a where a.status = 'FINISHED' and :date >= a.end and (a.customer.id = :userId or a.provider.id = :userId)")
    List<Appointment> findFinishedByUserIdWithEndBeforeDate(@Param("date") OffsetDateTime date, @Param("userId") UUID userId);

    @Query("select a from Appointment a where a.status = 'CONFIRMED' and a.customer.id = :customerId")
    List<Appointment> findConfirmedByCustomerId(@Param("customerId") UUID customerId);

    @Query("select a from Appointment a inner join a.work w where a.status = 'SCHEDULED' and a.customer.id <> :customerId and a.provider.id= :providerId and a.start >= :start and w.id = :workId")
    List<Appointment> getEligibleAppointmentsForExchange(@Param("start") OffsetDateTime start, @Param("customerId") UUID customerId, @Param("providerId") UUID providerId, @Param("workId") UUID workId);

    @Query("select a from Appointment a where a.status = 'EXCHANGE_REQUESTED' and a.start <= :start")
    List<Appointment> findExchangeRequestedWithStartBefore(@Param("start") OffsetDateTime date);

}
