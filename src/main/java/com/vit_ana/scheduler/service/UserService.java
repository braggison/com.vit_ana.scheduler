package com.vit_ana.scheduler.service;


import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.vit_ana.scheduler.entity.Work;
import com.vit_ana.scheduler.entity.user.Role;
import com.vit_ana.scheduler.entity.user.User;
import com.vit_ana.scheduler.entity.user.customer.CorporateCustomer;
import com.vit_ana.scheduler.entity.user.customer.Customer;
import com.vit_ana.scheduler.entity.user.customer.RetailCustomer;
import com.vit_ana.scheduler.entity.user.provider.Provider;
import com.vit_ana.scheduler.model.ChangePasswordForm;
import com.vit_ana.scheduler.model.UserForm;

public interface UserService {
    /*
     * User
     * */
    boolean userExists(String userName);

    User getUserById(UUID userId);

    User getUserByUsername(String userName);

    List<User> getUsersByRoleName(String roleName);

    List<User> getAllUsers();

    void deleteUserById(UUID userId);

    void updateUserPassword(ChangePasswordForm passwordChangeForm);

    /*
     * Provider
     * */
    Provider getProviderById(UUID providerId);

    List<Provider> getProvidersWithRetailWorks();

    List<Provider> getProvidersWithCorporateWorks();

    List<Provider> getProvidersByWork(Work work);

    List<Provider> getAllProviders();

    void saveNewProvider(UserForm userForm);

    void updateProviderProfile(UserForm updateData);

    Collection<Role> getRolesForProvider();

    /*
     * Customer
     * */
    Customer getCustomerById(UUID customerId);

    List<Customer> getAllCustomers();

    /*
     * RetailCustomer
     * */
    RetailCustomer getRetailCustomerById(UUID retailCustomerId);

    void saveNewRetailCustomer(UserForm userForm);

    void updateRetailCustomerProfile(UserForm updateData);

    Collection<Role> getRolesForRetailCustomer();

    /*
     * CorporateCustomer
     * */
    CorporateCustomer getCorporateCustomerById(UUID corporateCustomerId);

    List<RetailCustomer> getAllRetailCustomers();

    void saveNewCorporateCustomer(UserForm userForm);

    void updateCorporateCustomerProfile(UserForm updateData);

    Collection<Role> getRoleForCorporateCustomers();


}

