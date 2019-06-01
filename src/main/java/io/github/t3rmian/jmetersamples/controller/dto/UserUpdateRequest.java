package io.github.t3rmian.jmetersamples.controller.dto;

import io.github.t3rmian.jmetersamples.data.Profile;

import java.util.Set;

public class UserUpdateRequest extends UserRegistrationRequest {
    private Long id;
    private Set<ProfileUpdateRequest> profiles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<ProfileUpdateRequest> getProfiles() {
        return profiles;
    }

    public void setProfiles(Set<ProfileUpdateRequest> profiles) {
        this.profiles = profiles;
    }

    public static class ProfileUpdateRequest {
        private String id;
        private Profile.Type type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Profile.Type getType() {
            return type;
        }

        public void setType(Profile.Type type) {
            this.type = type;
        }
    }
}
