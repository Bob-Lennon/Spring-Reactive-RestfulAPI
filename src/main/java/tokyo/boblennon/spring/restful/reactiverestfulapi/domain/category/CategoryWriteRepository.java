package tokyo.boblennon.spring.restful.reactiverestfulapi.domain.category;

import reactor.core.publisher.Mono;

public interface CategoryWriteRepository {
    
    public Mono<Category> add(Category category);
    public Mono<Category> update(Category category);
    public Mono<Void> delete(Category category);

}
