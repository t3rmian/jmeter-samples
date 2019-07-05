package io.github.t3rmian.jmetersamples.controller.ws.dto;

import io.github.t3rmian.jmetersamples.controller.ws.WSEndpoint;

import javax.xml.bind.annotation.*;


/**
 * User context for the operation
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "userPayload", propOrder = {
        "id"
}, namespace = WSEndpoint.NAMESPACE_URI)
public class UserPayload {
    @XmlElement(required = true)
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
