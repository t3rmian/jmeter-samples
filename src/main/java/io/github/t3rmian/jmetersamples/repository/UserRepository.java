package io.github.t3rmian.jmetersamples.repository;

import io.github.t3rmian.jmetersamples.data.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Iterable<User> findByRemovalDateNotNull();
}
