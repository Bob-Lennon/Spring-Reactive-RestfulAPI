package tokyo.boblennon.spring.restful.reactiverestfulapi.infrastructure.product;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import tokyo.boblennon.spring.restful.reactiverestfulapi.domain.product.Product;

@Repository
public interface ProductMongoRepository extends ReactiveMongoRepository<Product, String> {

}
