package com.example.slabiak.appointmentscheduler.config;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

    @Bean
    public Flyway flyway(DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .locations("db/migration")
                .dataSource(dataSource)
                .schemas("public")
                .load();
        flyway.migrate();
        return flyway;
    }
}