package io.github.t3rmian.jmetersamples.repository;

import io.github.t3rmian.jmetersamples.data.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByIdAndRemovalDateIsNull(Long id);
    Optional<User> findByEmail(String email);
}
