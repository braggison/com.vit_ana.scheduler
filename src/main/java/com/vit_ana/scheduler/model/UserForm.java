package com.vit_ana.scheduler.model;

import java.util.List;
import java.util.UUID;

import com.vit_ana.scheduler.entity.Work;
import com.vit_ana.scheduler.entity.user.User;
import com.vit_ana.scheduler.entity.user.customer.CorporateCustomer;
import com.vit_ana.scheduler.entity.user.customer.RetailCustomer;
import com.vit_ana.scheduler.entity.user.provider.Provider;
import com.vit_ana.scheduler.validation.FieldsMatches;
import com.vit_ana.scheduler.validation.UniqueUsername;
import com.vit_ana.scheduler.validation.groups.CreateCorporateCustomer;
import com.vit_ana.scheduler.validation.groups.CreateProvider;
import com.vit_ana.scheduler.validation.groups.CreateUser;
import com.vit_ana.scheduler.validation.groups.UpdateCorporateCustomer;
import com.vit_ana.scheduler.validation.groups.UpdateProvider;
import com.vit_ana.scheduler.validation.groups.UpdateUser;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@FieldsMatches(field = "password", matchingField = "matchingPassword", groups = {CreateUser.class})
@Getter
@Setter
public class UserForm {

    @NotNull(groups = {UpdateUser.class})
    private UUID id;

    @UniqueUsername(groups = {CreateUser.class}, message = "{validation.UsernameAlreadyExists}")
    @Size(min = 5, max = 15, groups = {CreateUser.class}, message = "{validation.UsernameValidation}")
    @NotBlank(groups = {CreateUser.class})
    private String userName;

    @Size(min = 5, max = 15, groups = {CreateUser.class}, message = "{validation.PasswordValidation}")
    @NotBlank(groups = {CreateUser.class})
    private String password;

    @Size(min = 5, max = 15, groups = {CreateUser.class}, message = "{validation.PasswordValidation}")
    @NotBlank(groups = {CreateUser.class})
    private String matchingPassword;

    @NotBlank(groups = {CreateUser.class, UpdateUser.class}, message = "{validation.FirstnameCannotBeEmpty}")
    private String firstName;

    private String middleName;

    @NotBlank(groups = {CreateUser.class, UpdateUser.class}, message = "{validation.LastnameCannotBeEmpty}")
    private String lastName;

    @Email(groups = {CreateUser.class, UpdateUser.class}, message = "{validation.EmailNotValid}")
    @NotBlank(groups = {CreateUser.class, UpdateUser.class})
    private String email;

    @Pattern(groups = {CreateUser.class, UpdateUser.class}, regexp = "|8[0-9]{10}|\\+7[0-9]{10}", message = "{validation.PleaseEnterValidMobilePhone}")
    // Not required
    //@NotBlank(groups = {CreateUser.class, UpdateUser.class})
    private String mobile;

    //@Size(groups = {CreateUser.class, UpdateUser.class}, min = 5, max = 30, message = "Wrong street!")
    // Not required
    //@NotBlank(groups = {CreateUser.class, UpdateUser.class})
    private String street;

    @Pattern(groups = {CreateUser.class, UpdateUser.class}, regexp = "[0-9]{6}", message = "{validation.PleaseEnterValidPostcode}")
    // Not required
    //@NotBlank(groups = {CreateUser.class, UpdateUser.class})
    private String postcode;

    // Not required
    //@NotBlank(groups = {CreateUser.class, UpdateUser.class})
    private String city;

    /*
     * CorporateCustomer only:
     * */
    @NotBlank(groups = {CreateCorporateCustomer.class, UpdateCorporateCustomer.class}, message = "{validation.CompanyCannotBeEmpty}")
    private String companyName;

    @Pattern(groups = {CreateCorporateCustomer.class, UpdateCorporateCustomer.class}, regexp = "[0-9]{10}|[0-9]{12}", message = "{validation.PleaseEnterValidVatNumber}")
    @NotBlank(groups = {CreateCorporateCustomer.class, UpdateCorporateCustomer.class})
    private String vatNumber;

    /*
     * Provider only:
     * */
    @NotNull(groups = {CreateProvider.class, UpdateProvider.class})
    private List<Work> works;


    public UserForm() {
    }

    public UserForm(User user) {
        this.setId(user.getId());
        this.setUserName(user.getUserName());
        this.setFirstName(user.getFirstName());
        this.setMiddleName(user.getMiddleName());
        this.setLastName(user.getLastName());
        this.setEmail(user.getEmail());
        this.setCity(user.getCity());
        this.setStreet(user.getStreet());
        this.setPostcode(user.getPostcode());
        this.setMobile(user.getMobile());
    }

    public UserForm(Provider provider) {
        this((User) provider);
        this.setWorks(provider.getWorks());
    }

    public UserForm(RetailCustomer retailCustomer) {
        this((User) retailCustomer);
    }

    public UserForm(CorporateCustomer corporateCustomer) {
        this((User) corporateCustomer);
        this.setCompanyName(corporateCustomer.getCompanyName());
        this.setVatNumber(corporateCustomer.getVatNumber());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<Work> getWorks() {
        return works;
    }

    public void setWorks(List<Work> works) {
        this.works = works;
    }

}