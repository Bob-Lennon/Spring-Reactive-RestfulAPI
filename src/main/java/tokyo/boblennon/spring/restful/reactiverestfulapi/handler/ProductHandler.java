package tokyo.boblennon.spring.restful.reactiverestfulapi.handler;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.springframework.beans.factory.annotation.Autowired;
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
        .contentType(APPLICATION_JSON)
        .body(this.productRepositoryImp.getAll(), Product.class);
    }

    public Mono<ServerResponse>  get(ServerRequest request){
        String id = request.pathVariable("id");
        return this.productRepositoryImp.findById(id).flatMap(p -> ServerResponse
                .ok()
                .contentType(APPLICATION_JSON)
                .bodyValue(p))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

}
