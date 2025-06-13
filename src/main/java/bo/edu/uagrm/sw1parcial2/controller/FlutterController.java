package bo.edu.uagrm.sw1parcial2.controller;

import bo.edu.uagrm.sw1parcial2.service.FlutterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/flutter")
@CrossOrigin(origins = "https://flutter-ui-afaz.onrender.com")
public class FlutterController {

    private final FlutterService flutterService;

    @Value("${gemini.api.url}")
    private String url ;

    private final RestTemplate restTemplate = new RestTemplate();

    public FlutterController(FlutterService flutterService) {
        this.flutterService = flutterService;
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateFlutterCode(@RequestBody Object json) {
        String prompt = "Convierte el siguiente JSON de diseño en código Flutter, sin las explicaciones:\n\n" + json.toString();

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

    // Endpoint para descargar el archivo .zip
    @PostMapping("/download")
    //@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Resource> downloadFlutterProject(@RequestBody Map<String, String> body) throws IOException {
        String flutterCode = body.get("code");

        byte[] zipBytes = flutterService.generateFlutterZip(flutterCode);
        ByteArrayResource resource = new ByteArrayResource(zipBytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=flutter_project.zip")
                .contentLength(zipBytes.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}

