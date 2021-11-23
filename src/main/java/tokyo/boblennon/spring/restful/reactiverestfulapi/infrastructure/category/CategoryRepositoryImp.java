package tokyo.boblennon.spring.restful.reactiverestfulapi.infrastructure.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tokyo.boblennon.spring.restful.reactiverestfulapi.domain.category.Category;
import tokyo.boblennon.spring.restful.reactiverestfulapi.domain.category.CategoryReadRepository;
import tokyo.boblennon.spring.restful.reactiverestfulapi.domain.category.CategoryWriteRepository;

@Service
public class CategoryRepositoryImp implements CategoryReadRepository, CategoryWriteRepository {

    private final CategoryMongoRepository categoryMongoRepository;

    @Autowired
    public CategoryRepositoryImp(final CategoryMongoRepository categoryMongoRepository) {
        this.categoryMongoRepository = categoryMongoRepository;
    }

    @Override
    public Mono<Category> add(Category category) {
        return this.categoryMongoRepository.save(category);
    }

    @Override
    public Mono<Category> update(Category category) {
        return this.categoryMongoRepository.save(category);
    }

    @Override
    public Mono<Void> delete(Category category) {
        return this.categoryMongoRepository.delete(category);
    }

    @Override
    public Flux<Category> getAll() {
        return this.categoryMongoRepository.findAll();
    }

    @Override
    public Mono<Category> findById(String id) {
        return this.categoryMongoRepository.findById(id);
    }

}
