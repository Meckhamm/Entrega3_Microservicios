package cl.duoc.api_gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.web.servlet.function.RequestPredicates.path;

@Configuration
public class GatewayRoutesConfig {

    @Value("${PRODUCTO_SERVICE_URL:http://localhost:8081}")
    private String productoServiceUrl;

    @Value("${INVENTARIO_SERVICE_URL:http://localhost:8082}")
    private String inventarioServiceUrl;

    @Bean
    public RouterFunction<ServerResponse> productoRoute() {
        return route("producto-service")
                .route(path("/productos/**"), http())
                .before(uri(productoServiceUrl))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> inventarioRoute() {
        return route("inventario-service")
                .route(path("/inventario/**"), http())
                .before(uri(inventarioServiceUrl))
                .build();
    }
}
