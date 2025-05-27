package bo.edu.uagrm.sw1parcial2.service;


import bo.edu.uagrm.sw1parcial2.dto.RoleDTO;
import bo.edu.uagrm.sw1parcial2.dto.RoleResponseDTO;
import bo.edu.uagrm.sw1parcial2.model.Role;
import bo.edu.uagrm.sw1parcial2.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public RoleResponseDTO createRole(RoleDTO dto) {
        if (roleRepository.existsByName(dto.getName())){
            throw new IllegalArgumentException("El rol ya existe: " + dto.getName());
        }

        Role role = new Role();
        role.setName(dto.getName());

        Role savedRole = roleRepository.save(role);

        RoleResponseDTO response = new RoleResponseDTO();
        response.setId(savedRole.getId());
        response.setName(savedRole.getName());

        return response;
    }

    @Override
    public Role updateRole(Long id, Role updateRole) {

        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        existingRole.setName(updateRole.getName());

        return roleRepository.save(existingRole);
    }

    @Override
    public void deleteRole(Long id) {

        if (!roleRepository.existsById(id)){
            throw new RuntimeException("Role not found");
        }
        roleRepository.deleteById(id);
    }
}
