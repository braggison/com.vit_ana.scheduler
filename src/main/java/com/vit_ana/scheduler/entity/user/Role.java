package com.vit_ana.scheduler.entity.user;

import java.util.UUID;

import com.vit_ana.scheduler.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

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
