package bo.edu.uagrm.sw1parcial2.service;

import bo.edu.uagrm.sw1parcial2.dto.UserRoleRequest;
import bo.edu.uagrm.sw1parcial2.model.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User save(User user);

    boolean existsByUsername(String username);

    User update(Long id, User user);

    User asignRole(UserRoleRequest request);

    User removeRole(UserRoleRequest request);


    void delete(Long id);
}
