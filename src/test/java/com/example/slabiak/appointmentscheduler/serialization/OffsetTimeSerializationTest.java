package com.example.slabiak.appointmentscheduler.serialization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.OffsetTime;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

public class OffsetTimeSerializationTest {
	
	@Test
	void OffsetTimeSerialize() {
		OffsetTime offsetTime = OffsetTime.now();
		System.out.println(offsetTime);
		assertNotNull(offsetTime);
		offsetTime = OffsetTime.of(0, 0, 0, 0, ZoneOffset.UTC);
		System.out.println(offsetTime);
		assertEquals(offsetTime, OffsetTime.of(0, 0, 0, 0, ZoneOffset.UTC));
	}
}
