package com.example.slabiak.appointmentscheduler.entity.user;

import com.example.slabiak.appointmentscheduler.entity.BaseEntity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    @Column(name = "name")
    private String name;

    public Role() {
    	super(UUID.randomUUID());
    }

    public Role(String name) {
    	super(UUID.randomUUID());
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
