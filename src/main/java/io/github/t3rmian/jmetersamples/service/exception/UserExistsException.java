package io.github.t3rmian.jmetersamples.service.exception;

import java.text.MessageFormat;

public class UserExistsException extends ClientException {
    public UserExistsException(long id) {
        super(MessageFormat.format("User with id: {0} already exists", id));
    }
}
