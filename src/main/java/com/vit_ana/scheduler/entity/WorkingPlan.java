package com.vit_ana.scheduler.entity;

import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.TimeZone;
import java.util.UUID;

import org.hibernate.annotations.Type;

import com.vit_ana.scheduler.entity.user.User;
import com.vit_ana.scheduler.entity.user.provider.Provider;
import com.vit_ana.scheduler.model.DayPlan;
import com.vit_ana.scheduler.model.TimePeroid;
import com.vladmihalcea.hibernate.type.json.JsonType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

//@TypeDefs(@TypeDef(name = "json", typeClass = JsonStringType.class))
@Entity
@Table(name = "working_plans")
public class WorkingPlan {

    @Id
    @Column(name = "id_provider")
    private UUID id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "id_provider")
    private Provider provider;

    @Type(JsonType.class)
    @Column(columnDefinition = "json", name = "monday")
    private DayPlan monday;

    @Type(JsonType.class)
    @Column(columnDefinition = "json", name = "tuesday")
    private DayPlan tuesday;

    @Type(JsonType.class)
    @Column(columnDefinition = "json", name = "wednesday")
    private DayPlan wednesday;

    @Type(JsonType.class)
    @Column(columnDefinition = "json", name = "thursday")
    private DayPlan thursday;

    @Type(JsonType.class)
    @Column(columnDefinition = "json", name = "friday")
    private DayPlan friday;

    @Type(JsonType.class)
    @Column(columnDefinition = "json", name = "saturday")
    private DayPlan saturday;

    @Type(JsonType.class)
    @Column(columnDefinition = "json", name = "sunday")
    private DayPlan sunday;


    public WorkingPlan() {
    	this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public DayPlan getDay(String day) {
        switch (day) {
            case "monday":
                return monday;

            case "tuesday":
                return tuesday;

            case "wednesday":
                return wednesday;

            case "thursday":
                return thursday;

            case "friday":
                return friday;

            case "saturday":
                return saturday;

            case "sunday":
                return sunday;

            default:
                return null;
        }
    }

    public DayPlan getMonday() {
        return monday;
    }

    public void setMonday(DayPlan monday) {
        this.monday = monday;
    }

    public DayPlan getTuesday() {
        return tuesday;
    }

    public void setTuesday(DayPlan tuesday) {
        this.tuesday = tuesday;
    }

    public DayPlan getWednesday() {
        return wednesday;
    }

    public void setWednesday(DayPlan wednesday) {
        this.wednesday = wednesday;
    }

    public DayPlan getThursday() {
        return thursday;
    }

    public void setThursday(DayPlan thursday) {
        this.thursday = thursday;
    }

    public DayPlan getFriday() {
        return friday;
    }

    public void setFriday(DayPlan friday) {
        this.friday = friday;
    }

    public DayPlan getSaturday() {
        return saturday;
    }

    public void setSaturday(DayPlan saturday) {
        this.saturday = saturday;
    }

    public DayPlan getSunday() {
        return sunday;
    }

    public void setSunday(DayPlan sunday) {
        this.sunday = sunday;
    }


    public static WorkingPlan generateDefaultWorkingPlan() {
        WorkingPlan wp = new WorkingPlan();
        OffsetTime defaultStartHour = LocalTime.parse("06:00").atOffset(ZoneOffset.ofTotalSeconds(TimeZone.getDefault().getRawOffset()/1000));
        OffsetTime defaultEndHour = LocalTime.parse("18:00").atOffset(ZoneOffset.ofTotalSeconds(TimeZone.getDefault().getRawOffset()/1000));
        TimePeroid defaultWorkingPeroid = new TimePeroid(defaultStartHour, defaultEndHour);
        DayPlan defaultDayPlan = new DayPlan(defaultWorkingPeroid);
        wp.setMonday(defaultDayPlan);
        wp.setTuesday(defaultDayPlan);
        wp.setWednesday(defaultDayPlan);
        wp.setThursday(defaultDayPlan);
        wp.setFriday(defaultDayPlan);
        wp.setSaturday(defaultDayPlan);
        wp.setSunday(defaultDayPlan);
        return wp;
    }
}
