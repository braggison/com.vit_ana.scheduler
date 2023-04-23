package com.vit_ana.scheduler.model;

import java.time.OffsetTime;
import java.util.Objects;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimePeriod implements Comparable<TimePeriod> {

    private OffsetTime start;
    private OffsetTime end;
    private UUID availableAppointmentId;

    public TimePeriod() {

    }

    public TimePeriod(OffsetTime start, OffsetTime end) {
        this.start = start;
        this.end = end;
    }

    public TimePeriod(OffsetTime start, OffsetTime end, UUID availableAppointmentId) {
        this.start = start;
        this.end = end;
        this.availableAppointmentId = availableAppointmentId;
    }
    
    public OffsetTime getStart() {
        return start;
    }

    public void setStart(OffsetTime start) {
        this.start = start;
    }

    public OffsetTime getEnd() {
        return end;
    }

    public void setEnd(OffsetTime end) {
        this.end = end;
    }

    @Override
    public int compareTo(TimePeriod o) {
        return this.getStart().compareTo(o.getStart());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimePeriod period = (TimePeriod) o;
        return this.start.equals(period.getStart()) &&

                this.end.equals(period.getEnd());
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        return "TimePeriod{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
