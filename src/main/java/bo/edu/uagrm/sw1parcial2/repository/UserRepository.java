package bo.edu.uagrm.sw1parcial2.repository;

import bo.edu.uagrm.sw1parcial2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
}