package io.github.t3rmian.jmetersamples.controller;

import io.github.t3rmian.jmetersamples.controller.dto.UserRegistrationRequest;
import io.github.t3rmian.jmetersamples.controller.dto.UserUpdateRequest;
import io.github.t3rmian.jmetersamples.data.Profile;
import io.github.t3rmian.jmetersamples.data.User;
import io.github.t3rmian.jmetersamples.service.UserService;
import io.github.t3rmian.jmetersamples.service.exception.UserExistsException;
import io.github.t3rmian.jmetersamples.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/v1/users")
    public Iterable<User> getUsers() {
        return userService.findAll();
    }

    @GetMapping("/v1/users/{id}")
    public User getUserIncorrect(@PathVariable("id") long id) throws UserNotFoundException {
        return userService.findAll()
                .stream()
                .filter(u -> id == u.getId())
                .findAny()
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @GetMapping("/v2/users/{id}")
    public User getUserCorrect(@PathVariable("id") long id) throws UserNotFoundException {
        return userService.getUser(id);
    }

    @PutMapping("/v2/users")
    public void putUser(@RequestBody @Valid UserRegistrationRequest userRequest) throws UserExistsException {
        userService.registerUser(mapUserRequest(userRequest));
    }

    @PostMapping("/v2/users")
    public void postUser(@RequestBody @Valid UserUpdateRequest userRequest) throws UserNotFoundException {
        User user = mapUserRequest(userRequest);
        user.setId(userRequest.getId());
        Set<Profile> profiles = userRequest.getProfiles()
                .stream()
                .map(p -> userService.linkUserProfile(user, p.getType(), p.getId()))
                .collect(Collectors.toSet());
        user.setProfiles(profiles);
        userService.updateUser(user);
    }

    private User mapUserRequest(UserRegistrationRequest userRequest) {
        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setName(userRequest.getName());
        return user;
    }

    @PostMapping("/v2/users/{id}")
    public void deleteUser(@PathVariable("id") long id) throws UserNotFoundException {
        userService.deleteUser(id);
    }

}
