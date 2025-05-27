package bo.edu.uagrm.sw1parcial2.repository;

import bo.edu.uagrm.sw1parcial2.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
}