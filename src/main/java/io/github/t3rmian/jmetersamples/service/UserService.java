package io.github.t3rmian.jmetersamples.service;

import io.github.t3rmian.jmetersamples.data.Profile;
import io.github.t3rmian.jmetersamples.data.User;
import io.github.t3rmian.jmetersamples.repository.UserRepository;
import io.github.t3rmian.jmetersamples.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public User getUser(long id) throws UserNotFoundException {
        User user = userRepository.findByIdAndRemovalDateIsNull(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.getProfiles();
        return user;
    }

    @PostConstruct
    void prepareSamples() {
        User doe = new User();
        doe.setEmail("doe@example.com");
        doe.setName("doe");
        doe.setProfiles(new HashSet<>(Arrays.asList(
                new Profile("doeLinkedInId", Profile.Type.LINKEDIN),
                new Profile("doeTwitterId", Profile.Type.TWITTER)
        )));
        User smith = new User();
        smith.setEmail("smith@example.com");
        smith.setName("smith");
        User deletedMiller = new User();
        deletedMiller.setEmail("deletedMiller@example.com");
        deletedMiller.setName("deleted miller");
        deletedMiller.setRemovalDate(new Date());
        userRepository.saveAll(Stream.of(doe, smith, deletedMiller)
                .peek(u -> u.setRegistrationDate(new Date()))
                .collect(Collectors.toList())
        );
    }

    @Transactional
    public User registerUser(User userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setRegistrationDate(new Date());
        return userRepository.save(user);
    }

    @Transactional
    public void updateUser(User userDto) throws UserNotFoundException {
        User user = getUser(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setProfiles(userDto.getProfiles());
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(long id) throws UserNotFoundException {
        User user = getUser(id);
        user.setRemovalDate(new Date());
        userRepository.save(user);
    }
}
