package bo.edu.uagrm.sw1parcial2.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api/flutter")
public class FlutterController {

    @Value("${gemini.api.url}")
    private String url ;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/generate")
    public ResponseEntity<String> generateFlutterCode(@RequestBody Map<String, Object> json) {
        String prompt = "Convierte el siguiente JSON de diseño en código Flutter:\n\n" + json.toString();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> payload = new HashMap<>();
            Map<String, Object> message = new HashMap<>();
            message.put("role", "user");
            message.put("parts", Collections.singletonList(
                    Collections.singletonMap("text", prompt)
            ));

            payload.put("contents", Collections.singletonList(message));

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map content = (Map) ((List<?>) response.getBody().get("candidates")).get(0);
                Map contentParts = (Map) ((List<?>) ((Map) content.get("content")).get("parts")).get(0);
                String flutterCode = (String) contentParts.get("text");
                return ResponseEntity.ok(flutterCode);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se pudo generar código.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al contactar con Gemini.");
        }
    }
}

