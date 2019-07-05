package io.github.t3rmian.jmetersamples.controller.ws.dto;

import io.github.t3rmian.jmetersamples.controller.dto.UserUpdateRequest;
import io.github.t3rmian.jmetersamples.controller.ws.WSEndpoint;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "userUpdateRequestPayload", propOrder = {
        "userUpdateRequest",
        "userPayload"
}, namespace = WSEndpoint.NAMESPACE_URI)
public class UserUpdatePayload {
    @XmlElement(required = true)
    @XmlSchemaType(name = "userUpdateRequest", namespace = WSEndpoint.NAMESPACE_URI)
    private UserUpdateRequest userUpdateRequest;

    @XmlElement(required = true)
    @XmlSchemaType(name = "userPayload", namespace = WSEndpoint.NAMESPACE_URI)
    private UserPayload userPayload;

    public UserUpdateRequest getUserUpdateRequest() {
        return userUpdateRequest;
    }

    public void setUserUpdateRequest(UserUpdateRequest userUpdateRequest) {
        this.userUpdateRequest = userUpdateRequest;
    }

    public UserPayload getUserPayload() {
        return userPayload;
    }

    public void setUserPayload(UserPayload userPayload) {
        this.userPayload = userPayload;
    }
}

