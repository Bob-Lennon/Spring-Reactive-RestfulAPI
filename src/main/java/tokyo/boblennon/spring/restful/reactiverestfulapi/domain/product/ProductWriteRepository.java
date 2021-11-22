package tokyo.boblennon.spring.restful.reactiverestfulapi.domain.product;

import reactor.core.publisher.Mono;

public interface ProductWriteRepository {
    
    public Mono<Product> add(Product product);
    public Mono<Product> update(Product product);
    public Mono<Void> delete(Product product);
    
}
