package com.vit_ana.scheduler.model;


import java.util.UUID;

import com.vit_ana.scheduler.validation.CurrentPasswordMatches;
import com.vit_ana.scheduler.validation.FieldsMatches;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@FieldsMatches(field = "password", matchingField = "matchingPassword")
@CurrentPasswordMatches()
public class ChangePasswordForm {


    @NotNull
    private UUID id;

    @Size(min = 5, max = 10, message = "{validation.PasswordValidation}")
    @NotBlank()
    private String password;

    @Size(min = 5, max = 10, message = "{validation.PasswordValidation}")
    @NotBlank()
    private String matchingPassword;

    private String currentPassword;

    public ChangePasswordForm(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
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
}
