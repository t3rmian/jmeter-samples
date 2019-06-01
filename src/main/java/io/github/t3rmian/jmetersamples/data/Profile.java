package io.github.t3rmian.jmetersamples.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "PROFILES")
public class Profile {

    @EmbeddedId
    @JsonIgnore
    private ProfileID profileID;

    private Type type;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;


    public String getId() {
        return profileID.getId();
    }

    public ProfileID getProfileID() {
        return profileID;
    }

    public void setProfileID(ProfileID profileID) {
        this.profileID = profileID;
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

    @Embeddable
    public static class ProfileID implements Serializable {
        private String id;
        private Long userId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }
    }

    public enum Type {
        GITHUB, TWITTER, LINKEDIN
    }
}
