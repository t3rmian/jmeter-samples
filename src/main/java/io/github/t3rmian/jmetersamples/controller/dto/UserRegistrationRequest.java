package io.github.t3rmian.jmetersamples.controller.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "userRegistrationRequest", propOrder = {
        "name",
        "email"
})
public class UserRegistrationRequest {
    @XmlElement
    private String name;

    @Length(max = 50)
    @Email
    @XmlElement
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
