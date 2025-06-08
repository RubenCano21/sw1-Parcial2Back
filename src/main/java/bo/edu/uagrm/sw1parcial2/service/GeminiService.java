package bo.edu.uagrm.sw1parcial2.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String API_KEY;

    @Value("${gemini.api.url}")
    private String ENDPOINT;

    public String generarContenido (String prompt) {

        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> content = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(Map.of("text", prompt))
                        )
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(content, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(ENDPOINT, request, Map.class);
            Map body = response.getBody();
            List candidates = (List) body.get("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                Map candidate = (Map) candidates.get(0);
                Map contentMap = (Map) candidate.get("content");
                List parts = (List) contentMap.get("parts");
                Map firstPart = (Map) parts.get(0);
                return (String) firstPart.get("text");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Error generando contenido";
    }

    public Object generarComponentesFlutter( String prompt) {
        // Implementación para generar componentes de Flutter
        // INPUT
        // BOTON, TEXTO, IMAGEN, TEXTAREA, ETC
        // Optener el Json que genera el front y pasarlo a geminis para que genere el código en Flutter
        // obtener ese codigo y retornarlo como String y reconstruir el componente en Flutter
        // luego actualizar el estado del componente con el nuevo código generado en el proyecto de Flutter
        // finalmente, descargar un .zip con el proyecto de Flutter actualizado
        return generarContenido(prompt);
    }
}
