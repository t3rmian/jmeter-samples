package io.github.t3rmian.jmetersamples.controller;

import io.github.t3rmian.jmetersamples.data.User;
import io.github.t3rmian.jmetersamples.service.UserService;
import io.github.t3rmian.jmetersamples.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

/**
 * Undocumented API; demo for load testing
 */
@RestController
public class UserLoadRestController {

    private final static Logger logger = Logger.getAnonymousLogger();
    private final static AtomicLong findUserByEmailCount = new AtomicLong();
    private final UserService userService;

    @Autowired
    public UserLoadRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/v1/users/email/{email}")
    public User findUserByEmail(@PathVariable("email") String email) throws UserNotFoundException {
        return callService(() -> userService.findUserByEmail(email), ThreadLocalRandom.current().nextInt(500, 1500));
    }

    @GetMapping("/v2/users/email/{email}")
    public User findUserByEmail_StaticWaitTime(@PathVariable("email") String email) throws UserNotFoundException {
        return callService(() -> userService.findUserByEmail(email), 1500);
    }

    @Transactional
    @GetMapping("/v3/users/email/{email}")
    public User findUserByEmail_StaticWaitTime_Transactional(@PathVariable("email") String email) throws UserNotFoundException {
        return callService(() -> userService.findUserByEmail_Transactional(email, 1500), 0);
    }

    private User callService(Callable<User> callable, int simulatedWaitMs) {
        long count = findUserByEmailCount.incrementAndGet();
        long startTime = System.currentTimeMillis();
        logger.info(String.format("findUserByEmail execution number %d start", count));
        try {
            Thread.sleep(simulatedWaitMs);
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            logger.info(String.format("findUserByEmail execution number %d end: %d ms", count, System.currentTimeMillis() - startTime));
        }
    }
}
