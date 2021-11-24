package tokyo.boblennon.spring.restful.reactiverestfulapi.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;
import tokyo.boblennon.spring.restful.reactiverestfulapi.domain.product.Product;
import tokyo.boblennon.spring.restful.reactiverestfulapi.infrastructure.product.ProductRepositoryImp;

@Component
public class ProductHandler {
    
    private final ProductRepositoryImp productRepositoryImp;

    @Autowired
    public ProductHandler(final ProductRepositoryImp productRepositoryImp){
        this.productRepositoryImp = productRepositoryImp;
    }

    public Mono<ServerResponse> getAll(ServerRequest request){
        return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(this.productRepositoryImp.getAll(), Product.class);
    }

    

}
