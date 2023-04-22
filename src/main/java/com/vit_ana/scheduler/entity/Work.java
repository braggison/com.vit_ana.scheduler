package com.vit_ana.scheduler.entity;

import java.util.List;
import java.util.UUID;

import com.vit_ana.scheduler.entity.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "works")
@Getter
@Setter
public class Work extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private double price;

    @Column(name = "duration")
    private int duration;

    @Column(name = "editable")
    private boolean editable;

    @Column(name = "is_use_slots")
    private Boolean isUseSlots;
    
    @Column(name = "target")
    private String targetCustomer;

    @ManyToMany
    @JoinTable(name = "works_providers", joinColumns = @JoinColumn(name = "id_work"), inverseJoinColumns = @JoinColumn(name = "id_user"))
    private List<User> providers;

    public Work() {
    	super(UUID.randomUUID());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<User> getProviders() {
        return providers;
    }

    public void setProviders(List<User> providers) {
        this.providers = providers;
    }

    public boolean getEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
    
    public Boolean getIsUseSlots() {
    	return Boolean.TRUE.equals(isUseSlots);
    }

    public void setIsUseSlots(Boolean isUseSlots) {
    	this.isUseSlots = Boolean.TRUE.equals(isUseSlots);
    }

    public String getTargetCustomer() {
        return targetCustomer;
    }

    public void setTargetCustomer(String targetCustomer) {
        this.targetCustomer = targetCustomer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Work)) return false;
        Work work = (Work) o;
        return super.getId().equals(work.getId());
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }
}
