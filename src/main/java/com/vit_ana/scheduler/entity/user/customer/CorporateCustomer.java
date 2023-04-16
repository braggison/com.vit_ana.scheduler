package com.vit_ana.scheduler.entity.user.customer;

import java.util.Collection;

import com.vit_ana.scheduler.entity.user.Role;
import com.vit_ana.scheduler.model.UserForm;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "corporate_customers")
@PrimaryKeyJoinColumn(name = "id_customer")
public class CorporateCustomer extends Customer {

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "vat_number")
    private String vatNumber;


    public CorporateCustomer() {
    }

    public CorporateCustomer(UserForm userFormDTO, String encryptedPassword, Collection<Role> roles) {
        super(userFormDTO, encryptedPassword, roles);
        this.companyName = userFormDTO.getCompanyName();
        this.vatNumber = userFormDTO.getVatNumber();
    }

    @Override
    public void update(UserForm updateData) {
        super.update(updateData);
        this.companyName = updateData.getCompanyName();
        this.vatNumber = updateData.getVatNumber();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

}
