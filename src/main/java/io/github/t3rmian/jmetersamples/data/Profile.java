package io.github.t3rmian.jmetersamples.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "PROFILES", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"externalId", "user_id", "type"})
})
public class Profile {

    @Id
    @GeneratedValue
    private Long id;

    private String externalId;

    private Type type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
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

    public enum Type {
        GITHUB, TWITTER, LINKEDIN
    }
}
