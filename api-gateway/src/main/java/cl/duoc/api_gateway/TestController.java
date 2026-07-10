package cl.duoc.api_gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/gateway")
public class TestController {

    @GetMapping("/test/verificar")
    public Map<String, String> verificarGateway() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("mensaje", "API Gateway respondiendo y ruteando correctamente de forma individual");
        return response;
    }
}