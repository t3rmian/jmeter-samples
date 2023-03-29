package io.github.t3rmian.jmetersamples.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

@Entity
@Table(name = "PROFILES", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"externalId", "user_id", "type"})
})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "profile", propOrder = {
        "id",
        "externalId",
        "type"
})
public class Profile {

    @Id
    @GeneratedValue
    @XmlElement(required = true)
    private Long id;

    @XmlElement(required = true)
    private String externalId;

    @XmlElement(required = true)
    @XmlSchemaType(name = "profileType", namespace = "https://github.com/t3rmian/jmeter-samples")
    private Type type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @XmlTransient
    private User user;

    public Profile() {
    }

    public Profile(String externalId, Type type) {
        this.externalId = externalId;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @XmlEnum
    @XmlType(name = "profileType", namespace = "https://github.com/t3rmian/jmeter-samples")
    public enum Type {
        GITHUB, TWITTER, LINKEDIN
    }
}
