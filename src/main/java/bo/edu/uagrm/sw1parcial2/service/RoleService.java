package bo.edu.uagrm.sw1parcial2.service;

import bo.edu.uagrm.sw1parcial2.dto.RoleDTO;
import bo.edu.uagrm.sw1parcial2.dto.RoleResponseDTO;
import bo.edu.uagrm.sw1parcial2.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    List<Role> getAllRoles();

    Optional<Role> getRoleById(Long id);

    RoleResponseDTO createRole(RoleDTO roleDTO);

    Role updateRole(Long id, Role updateRole);

    void deleteRole(Long id);
}
