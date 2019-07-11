package io.github.t3rmian.jmetersamples.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.t3rmian.jmetersamples.controller.ws.WSEndpoint;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "USERS")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "user", propOrder = {
        "id",
        "name",
        "email",
        "registrationDate",
        "profiles"
})
public class User {
    @Id
    @GeneratedValue
    @XmlElement(required = true)
    private Long id;

    @XmlElement
    private String name;

    @Column(unique = true, length = 50)
    @XmlElement(required = true)
    private String email;

    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    private Date registrationDate;

    @JsonIgnore
    @XmlTransient
    private Date removalDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @XmlElement
    @XmlSchemaType(name = "profile", namespace = WSEndpoint.NAMESPACE_URI)
    private Set<Profile> profiles;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getRemovalDate() {
        return removalDate;
    }

    public void setRemovalDate(Date removalDate) {
        this.removalDate = removalDate;
    }

    public Set<Profile> getProfiles() {
        return profiles == null ? Collections.emptySet() : profiles;
    }

    public void setProfiles(Set<Profile> profiles) {
        if (this.profiles != null) {
            this.profiles.clear();
            this.profiles.addAll(profiles);
        } else {
            this.profiles = profiles;
        }
        profiles.forEach(p -> p.setUser(this));
    }
}
