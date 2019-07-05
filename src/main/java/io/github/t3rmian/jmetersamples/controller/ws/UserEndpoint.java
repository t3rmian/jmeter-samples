package io.github.t3rmian.jmetersamples.controller.ws;

import io.github.t3rmian.jmetersamples.controller.dto.UserRegistrationRequest;
import io.github.t3rmian.jmetersamples.controller.ws.dto.ObjectFactory;
import io.github.t3rmian.jmetersamples.controller.ws.dto.SuccessResponse;
import io.github.t3rmian.jmetersamples.controller.ws.dto.UserPayload;
import io.github.t3rmian.jmetersamples.controller.ws.dto.UserUpdatePayload;
import io.github.t3rmian.jmetersamples.data.Profile;
import io.github.t3rmian.jmetersamples.data.User;
import io.github.t3rmian.jmetersamples.service.UserService;
import io.github.t3rmian.jmetersamples.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.bind.JAXBElement;
import java.util.Set;
import java.util.stream.Collectors;

import static io.github.t3rmian.jmetersamples.controller.ws.WSEndpoint.NAMESPACE_URI;

@Endpoint
public class UserEndpoint {
    private UserService userService;
    private ObjectFactory objectFactory;

    @Autowired
    public UserEndpoint(UserService userService) {
        this.userService = userService;
        this.objectFactory = new ObjectFactory();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUserIncorrectRequest")
    @ResponsePayload
    public JAXBElement<User> getUserIncorrect(@RequestPayload UserPayload userPayload) {
        return userService.findAll()
                .stream()
                .filter(u -> userPayload.getId() == u.getId())
                .findAny()
                .map(u -> objectFactory.createGetUserIncorrectResponse(u))
                .orElseThrow(() -> new UserNotFoundException(userPayload.getId()));
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUserRequest")
    @ResponsePayload
    public JAXBElement<User> getUser(@RequestPayload UserPayload userPayload) {
        User user = userService.getUser(userPayload.getId());
        return objectFactory.createGetUserResponse(user);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteUserRequest")
    @ResponsePayload
    public SuccessResponse deleteUser(@RequestPayload UserPayload userPayload) {
        userService.deleteUser(userPayload.getId());
        return new SuccessResponse();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "registerUserRequest")
    @ResponsePayload
    public UserPayload registerUser(@RequestPayload UserRegistrationRequest userRequest) {
        User user = userService.registerUser(mapUserRequest(userRequest));
        UserPayload response = new UserPayload();
        response.setId(user.getId());
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateUserRequest")
    @ResponsePayload
    public void updateUser(@RequestPayload UserUpdatePayload userUpdatePayload) throws UserNotFoundException {
        User user = mapUserRequest(userUpdatePayload.getUserUpdateRequest());
        user.setId(userUpdatePayload.getUserPayload().getId());
        Set<Profile> profiles = userUpdatePayload.getUserUpdateRequest().getProfiles()
                .stream()
                .map(p -> new Profile(p.getExternalId(), p.getType()))
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

}
