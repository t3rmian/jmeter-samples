package io.github.t3rmian.jmetersamples.controller.dto;

import io.github.t3rmian.jmetersamples.controller.ws.WSEndpoint;
import io.github.t3rmian.jmetersamples.data.Profile;

import javax.xml.bind.annotation.*;
import java.util.Collections;
import java.util.Set;

/**
 * User data to be updated (old data will be removed e.g. profiles)
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "userUpdateRequest", propOrder = {
        "profiles"
})
public class UserUpdateRequest extends UserRegistrationRequest {
    @XmlElement
    @XmlSchemaType(name = "profileUpdateRequest", namespace = WSEndpoint.NAMESPACE_URI)
    private Set<ProfileUpdateRequest> profiles;

    public Set<ProfileUpdateRequest> getProfiles() {
        return profiles == null ? Collections.emptySet() : profiles;
    }

    public void setProfiles(Set<ProfileUpdateRequest> profiles) {
        this.profiles = profiles;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "profileUpdateRequest", propOrder = {
            "externalId",
            "type"
    }, namespace = WSEndpoint.NAMESPACE_URI)
    public static class ProfileUpdateRequest {
        @XmlElement(required = true)
        private String externalId;
        @XmlElement(required = true)
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
