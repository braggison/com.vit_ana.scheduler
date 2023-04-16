package com.vit_ana.scheduler.entity.user.customer;

import java.util.Collection;
import java.util.List;

import com.vit_ana.scheduler.entity.Appointment;
import com.vit_ana.scheduler.entity.user.Role;
import com.vit_ana.scheduler.entity.user.User;
import com.vit_ana.scheduler.model.UserForm;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers")
@PrimaryKeyJoinColumn(name = "id_customer")
public class Customer extends User {

    @OneToMany(mappedBy = "customer")
    private List<Appointment> appointments;

    public Customer() {
    }

    public Customer(UserForm userFormDTO, String encryptedPassword, Collection<Role> roles) {
        super(userFormDTO, encryptedPassword, roles);
    }


    public String getType() {
        if (super.hasRole("ROLE_CUSTOMER_CORPORATE")) {
            return "corporate";
        } else if (super.hasRole("ROLE_CUSTOMER_RETAIL")) {
            return "retail";
        }
        return "";
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }
}
