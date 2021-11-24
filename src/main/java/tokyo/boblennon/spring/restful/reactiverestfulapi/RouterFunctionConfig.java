package tokyo.boblennon.spring.restful.reactiverestfulapi;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import tokyo.boblennon.spring.restful.reactiverestfulapi.handler.ProductHandler;

@Configuration
public class RouterFunctionConfig {

    @Bean
    public RouterFunction<ServerResponse> routes(ProductHandler productHandler ){
        RouterFunction<ServerResponse> route = route(
                GET("/api/v2/products")
                    .or(GET("/api/get-products")), productHandler::getAll)
                .andRoute(GET("/api/v2/products/{id}"), productHandler::get)
                .andRoute(GET("/api/v3/products/{id}")
                    .and(RequestPredicates.contentType(APPLICATION_JSON)),
                    productHandler::get);
            return route;
    }
    
}
