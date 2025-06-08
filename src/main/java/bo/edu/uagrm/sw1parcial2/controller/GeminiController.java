package bo.edu.uagrm.sw1parcial2.controller;

import bo.edu.uagrm.sw1parcial2.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/gemini")
@RequiredArgsConstructor
public class GeminiController {

    private final GeminiService geminiService;

    @PostMapping("/generate")
    public ResponseEntity<String> generate(@RequestBody Map<String, String> request) {
        String prompt = request.get("prompt");
        String respuesta = geminiService.generarContenido(prompt);
        return ResponseEntity.ok(respuesta);
    }
}
