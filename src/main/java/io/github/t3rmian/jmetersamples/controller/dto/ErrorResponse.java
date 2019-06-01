package io.github.t3rmian.jmetersamples.controller.dto;

import java.util.Date;

public class ErrorResponse {
    private String error;
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
