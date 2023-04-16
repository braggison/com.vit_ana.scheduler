package com.vit_ana.scheduler.model;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.vit_ana.scheduler.entity.Appointment;
import com.vit_ana.scheduler.entity.AppointmentStatus;

public class AppointmentSerializer extends StdSerializer<Appointment> {

    public AppointmentSerializer() {
        this(null);
    }

    public AppointmentSerializer(Class<Appointment> t) {
        super(t);
    }

    @Override
    public void serialize(Appointment appointment, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("id", appointment.getId().toString());
        gen.writeStringField("title", appointment.getWork().getName());
        gen.writeNumberField("start", appointment.getStart().toInstant().toEpochMilli());
        gen.writeNumberField("end", appointment.getEnd().toInstant().toEpochMilli());
        gen.writeStringField("url", "/appointments/" + appointment.getId());
        gen.writeStringField("color", appointment.getStatus().equals(AppointmentStatus.SCHEDULED) ? "#28a745" : "grey");
        gen.writeEndObject();
    }
}
