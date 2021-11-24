package tokyo.boblennon.spring.restful.reactiverestfulapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import tokyo.boblennon.spring.restful.reactiverestfulapi.domain.product.Product;
import tokyo.boblennon.spring.restful.reactiverestfulapi.infrastructure.product.ProductRepositoryImp;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RouterFunctionConfig {

    private final ProductRepositoryImp productRepositoryImp;

    @Autowired
    public RouterFunctionConfig(final ProductRepositoryImp productRepositoryImp){
        this.productRepositoryImp = productRepositoryImp;
    }

    @Bean
    public RouterFunction<ServerResponse> routes(){
        return route(GET("/api/v2/products"), request -> {
            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(this.productRepositoryImp.getAll(), Product.class);
        });
    }
    
}
