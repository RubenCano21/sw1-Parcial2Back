package bo.edu.uagrm.sw1parcial2.repository;

import bo.edu.uagrm.sw1parcial2.model.Role;
import bo.edu.uagrm.sw1parcial2.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName roleName);

    boolean existsByName(RoleName name);
}
