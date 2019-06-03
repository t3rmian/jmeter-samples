package io.github.t3rmian.jmetersamples.controller;

import io.github.t3rmian.jmetersamples.controller.dto.ErrorResponse;
import io.github.t3rmian.jmetersamples.service.exception.ClientException;
import io.github.t3rmian.jmetersamples.service.exception.UserExistsException;
import io.github.t3rmian.jmetersamples.service.exception.UserNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;
import java.util.regex.Pattern;

@ControllerAdvice
public class RestErrorHandler {

    @ExceptionHandler(ClientException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse processClientException(ClientException ce) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(ce.getMessage());
        errorResponse.setTime(new Date());
        return errorResponse;
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse processNotFoundException(UserNotFoundException unfe) {
        return processClientException(unfe);
    }

    @ExceptionHandler(UserExistsException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorResponse processExistsException(UserExistsException uee) {
        return processClientException(uee);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ResponseBody
    // not a good practice but this is a sample, consider https://stackoverflow.com/questions/2109476/how-to-handle-dataintegrityviolationexception-in-spring
    public ErrorResponse processDataIntegrityViolationException(DataIntegrityViolationException dive) {
        String message = dive.getMessage();
        if (message != null && Pattern.compile(".*constraint.*USERS\\(EMAIL\\).*").matcher(message).find()) {
            return processClientException(new ClientException("Email already registered"));
        } else {
            return processClientException(new ClientException("Data integrity violation exception: " + message));
        }
    }
}
