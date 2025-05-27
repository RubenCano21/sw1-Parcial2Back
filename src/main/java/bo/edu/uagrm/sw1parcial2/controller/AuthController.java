package bo.edu.uagrm.sw1parcial2.controller;

import bo.edu.uagrm.sw1parcial2.model.User;
import bo.edu.uagrm.sw1parcial2.repository.UserRepository;
import bo.edu.uagrm.sw1parcial2.model.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository usuarioRepository;

    public AuthController(UserRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> usuarioOpt = usuarioRepository.findByUsername(request.getCorreo());

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Usuario no encontrado");
        }

        User user = usuarioOpt.get();

        // Aquí pones la validación de la contraseña:
        if (!user.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(401).body("Contraseña incorrecta");
        }

        // Si todo está bien:
        return ResponseEntity.ok("Login exitoso");
    }
}
