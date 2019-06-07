package io.github.t3rmian.jmetersamples.controller.dto;

import io.github.t3rmian.jmetersamples.data.Profile;

import java.util.Collections;
import java.util.Set;

public class UserUpdateRequest extends UserRegistrationRequest {
    private Set<ProfileUpdateRequest> profiles;

    public Set<ProfileUpdateRequest> getProfiles() {
        return profiles == null ? Collections.emptySet() : profiles;
    }

    public void setProfiles(Set<ProfileUpdateRequest> profiles) {
        this.profiles = profiles;
    }

    public static class ProfileUpdateRequest {
        private String externalId;
        private Profile.Type type;

        public String getExternalId() {
            return externalId;
        }

        public void setExternalId(String externalId) {
            this.externalId = externalId;
        }

        public Profile.Type getType() {
            return type;
        }

        public void setType(Profile.Type type) {
            this.type = type;
        }
    }
}
