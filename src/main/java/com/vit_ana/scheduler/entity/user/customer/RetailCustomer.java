package com.vit_ana.scheduler.entity.user.customer;

import java.util.Collection;

import com.vit_ana.scheduler.entity.user.Role;
import com.vit_ana.scheduler.model.UserForm;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "retail_customers")
@PrimaryKeyJoinColumn(name = "id_customer")
public class RetailCustomer extends Customer {

    public RetailCustomer() {
    }

    public RetailCustomer(UserForm userFormDTO, String encryptedPassword, Collection<Role> roles) {
        super(userFormDTO, encryptedPassword, roles);
    }


}
