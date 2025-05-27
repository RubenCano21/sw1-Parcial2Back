package bo.edu.uagrm.sw1parcial2.service;

import bo.edu.uagrm.sw1parcial2.dto.UserRoleRequest;
import bo.edu.uagrm.sw1parcial2.model.Role;
import bo.edu.uagrm.sw1parcial2.model.RoleName;
import bo.edu.uagrm.sw1parcial2.model.User;
import bo.edu.uagrm.sw1parcial2.repository.RoleRepository;
import bo.edu.uagrm.sw1parcial2.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return  repository.findAll();
    }

    @Override
    @Transactional
    public User save(User user) {

        Optional<Role> optionalRoleUser = roleRepository.findByName(RoleName.ROLE_USER);
        List<Role> roles = new ArrayList<>();

        optionalRoleUser.ifPresent(roles::add);

        if (user.isAdmin()) {
            Optional<Role> optionalRoleAdmin = roleRepository.findByName(RoleName.ROLE_ADMIN);
            optionalRoleAdmin.ifPresent(roles::add);
        }

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public User update(Long id, User user) {
        Optional<User> optionalUser = repository.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setUsername(user.getUsername());
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            existingUser.setAdmin(user.isAdmin());
            return repository.save(existingUser);
        }
        return null;
    }

    @Override
    public User asignRole(UserRoleRequest request) {
        User user = repository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User no encontrado"));

        Role role = roleRepository.findByName(request.getRoleName())
                .orElseThrow(() -> new RuntimeException("Role no encontrado"));

        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            repository.save(user);
        }
        return user;
    }

    @Override
    public User removeRole(UserRoleRequest request) {
        User user = repository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User no encontrado"));

        Role role = roleRepository.findByName(request.getRoleName())
                .orElseThrow(() -> new RuntimeException("Role no encontrado"));

        if (!user.getRoles().contains(role)) {
            user.getRoles().remove(role);
            repository.save(user);
        }
        return user;
    }

    @Override
    public void delete(Long id) {

        Optional<User> optionalUser = repository.findById(id);
        optionalUser.ifPresent(repository::delete);
    }


}
