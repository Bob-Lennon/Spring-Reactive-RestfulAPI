package tokyo.boblennon.spring.restful.reactiverestfulapi.infrastructure.category;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import tokyo.boblennon.spring.restful.reactiverestfulapi.domain.category.Category;

@Repository
public interface CategoryMongoRepository extends ReactiveMongoRepository<Category, String> {

}
