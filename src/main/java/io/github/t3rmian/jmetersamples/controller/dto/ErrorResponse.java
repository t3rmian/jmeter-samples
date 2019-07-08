package io.github.t3rmian.jmetersamples.controller.dto;

import javax.xml.bind.annotation.*;
import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "errorFault", propOrder = {
        "error",
        "time"
})
public class ErrorResponse {
    @XmlElement
    private String error;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    private Date time;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
