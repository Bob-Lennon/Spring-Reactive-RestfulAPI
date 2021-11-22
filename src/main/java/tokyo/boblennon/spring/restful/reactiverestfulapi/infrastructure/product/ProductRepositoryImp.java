package tokyo.boblennon.spring.restful.reactiverestfulapi.infrastructure.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tokyo.boblennon.spring.restful.reactiverestfulapi.domain.product.Product;
import tokyo.boblennon.spring.restful.reactiverestfulapi.domain.product.ProductReadRepository;
import tokyo.boblennon.spring.restful.reactiverestfulapi.domain.product.ProductWriteRepository;

@Service
public class ProductRepositoryImp implements ProductReadRepository, ProductWriteRepository {

    private final ProductMongoRepository productMongoRepository;

    @Autowired
    public ProductRepositoryImp(final ProductMongoRepository productMongoRepository) {
        this.productMongoRepository = productMongoRepository;
    }

    @Override
    public Mono<Product> add(Product product) {
        return productMongoRepository.save(product);
    }

    @Override
    public Mono<Product> update(Product product) {
        return productMongoRepository.save(product);
    }

    @Override
    public Mono<Void> delete(Product product) {
        return productMongoRepository.delete(product);
    }

    @Override
    public Flux<Product> getAll() {
        return productMongoRepository.findAll();
    }

    @Override
    public Flux<Product> getAllByNameUpperCase() {
        return productMongoRepository.findAll().map(p ->{
            p.setName(p.getName().toUpperCase());
            return p;
        });
    }
    
    @Override
    public Flux<Product> getAllByNameUpperCaseRepeat() {
        return productMongoRepository.findAll().map(p ->{
            p.setName(p.getName().toUpperCase());
            return p;
        })
            .repeat(10000);
    }

    @Override
    public Mono<Product> findById(String id) {
        return productMongoRepository.findById(id);
    }



}