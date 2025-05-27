package bo.edu.uagrm.sw1parcial2.controller;

import bo.edu.uagrm.sw1parcial2.model.Usuario;
import bo.edu.uagrm.sw1parcial2.repository.UsuarioRepository;
import bo.edu.uagrm.sw1parcial2.model.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UsuarioRepository usuarioRepository;

    public AuthController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(request.getCorreo());

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Usuario no encontrado");
        }

        Usuario usuario = usuarioOpt.get();

        // Aquí pones la validación de la contraseña:
        if (!usuario.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(401).body("Contraseña incorrecta");
        }

        // Si todo está bien:
        return ResponseEntity.ok("Login exitoso");
    }
}
