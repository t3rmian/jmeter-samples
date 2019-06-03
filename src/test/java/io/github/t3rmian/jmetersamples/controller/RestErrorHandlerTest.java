package io.github.t3rmian.jmetersamples.controller;

import io.github.t3rmian.jmetersamples.JmeterSamplesApplication;
import io.github.t3rmian.jmetersamples.controller.dto.ErrorResponse;
import io.github.t3rmian.jmetersamples.data.User;
import io.github.t3rmian.jmetersamples.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JmeterSamplesApplication.class})
public class RestErrorHandlerTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void processDataIntegrityViolationException() {
        User user = new User();
        user.setEmail("email-unique-check@example.com");
        userRepository.save(user);
        User user2 = new User();
        user2.setEmail("email-unique-check@example.com");
        try {
            userRepository.save(user2);
            fail("Expected DataIntegrityViolationException");
        } catch (DataIntegrityViolationException dive) {
            assertThat(dive.getMessage(), allOf(
                    containsString("could not execute statement"),
                    containsString("constraint"),
                    containsString("ON PUBLIC.USERS(EMAIL)"))
            );
            ErrorResponse errorResponse = new RestErrorHandler()
                    .processDataIntegrityViolationException(
                            new DataIntegrityViolationException("could not execute statement constraint ON PUBLIC.USERS(EMAIL)")
                    );
            assertThat(errorResponse.getError(), containsString("Email already registered"));
        }
    }
}