package io.github.t3rmian.jmetersamples.controller;

import io.github.t3rmian.jmetersamples.data.User;
import io.github.t3rmian.jmetersamples.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/v1/users")
    public Iterable<User> getUsersIncorrect() {
        return userService.findAll();
    }

    @GetMapping("/v2/users")
    public Iterable<User> getUsersCorrect() {
        return userService.findAll();
    }

}
