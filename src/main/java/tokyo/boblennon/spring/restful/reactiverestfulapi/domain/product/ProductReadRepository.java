package tokyo.boblennon.spring.restful.reactiverestfulapi.domain.product;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductReadRepository {
    
    public Flux<Product> getAll();
    public Flux<Product> getAllByNameUpperCase();
    public Flux<Product> getAllByNameUpperCaseRepeat();
    public Mono<Product> findById(String id);

}
