package bo.edu.uagrm.sw1parcial2.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

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


    private final String promptSistema = """
        Eres un generador de interfaces Flutter. Devuélveme únicamente un JSON válido con la estructura de los componentes necesarios. 
        La estructura debe comenzar con un contenedor del tipo "Column". Los componentes disponibles son:

        - Text: debe tener el atributo `text`.
        - TextField: debe tener el atributo `placeholder`.
        - Button: debe tener el atributo `text`.

        Ejemplo:

        {
          "type": "Column",
          "children": [
            { "type": "Text", "text": "Hola" },
            { "type": "TextField", "placeholder": "Correo electrónico" },
            { "type": "Button", "text": "Enviar" }
          ]
        }

        No incluyas explicaciones, solo devuelve el JSON. Ahora genera la interfaz para:
        """;

    public String generarJsonUI (String userPrompt) {
        RestTemplate restTemplate = new RestTemplate();

        String content = promptSistema + " " + userPrompt;

        JSONObject requestBody = new JSONObject();
        requestBody.put("contents", new Object[] {
                Map.of("parts", new Object[] {
                        Map.of("text", content) })
        });

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
       // headers.setBearerAuth(API_KEY);

        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(ENDPOINT, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            // Extrae el texto generado del cuerpo JSON
            JSONObject json = new JSONObject(response.getBody());
            return json
                    .getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text");
        } else {
            throw new RuntimeException("Error al llamar a Gemini: " + response.getStatusCode());
        }
    }


}
