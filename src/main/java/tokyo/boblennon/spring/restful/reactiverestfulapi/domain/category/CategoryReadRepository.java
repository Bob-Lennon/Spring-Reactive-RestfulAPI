package tokyo.boblennon.spring.restful.reactiverestfulapi.domain.category;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CategoryReadRepository {
    
    public Flux<Category> getAll();
    public Mono<Category> findById(String id);
    
}
