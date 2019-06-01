package io.github.t3rmian.jmetersamples.service.exception;

import java.text.MessageFormat;

public class UserNotFoundException extends ClientException {
    public UserNotFoundException(long id) {
        super(MessageFormat.format("User with id {0} not found", id));
    }
}
