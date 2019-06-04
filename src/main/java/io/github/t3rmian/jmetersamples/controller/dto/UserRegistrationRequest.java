package io.github.t3rmian.jmetersamples.controller.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;

public class UserRegistrationRequest {
    private String name;

    @Length(max = 50)
    @Email
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
