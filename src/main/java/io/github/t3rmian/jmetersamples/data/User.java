package io.github.t3rmian.jmetersamples.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Column(unique = true, length = 50)
    private String email;

    private Date registrationDate;

    @JsonIgnore
    private Date removalDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
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
        this.profiles = profiles;
        profiles.forEach(p -> p.setUser(this));
    }
}
