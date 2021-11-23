package tokyo.boblennon.spring.restful.reactiverestfulapi.controller;

import java.net.URI;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tokyo.boblennon.spring.restful.reactiverestfulapi.domain.product.Product;
import tokyo.boblennon.spring.restful.reactiverestfulapi.infrastructure.product.ProductRepositoryImp;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepositoryImp productRepositoryImp;

    @Autowired
    public ProductController(final ProductRepositoryImp productRepositoryImp) {
        this.productRepositoryImp = productRepositoryImp;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Flux<Product>>> getAll() {
        return Mono.just(ResponseEntity.ok().body(this.productRepositoryImp
                .getAll()));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Product>> add(@RequestBody Product product){
        if(product.getCreatedAt() == null)
            product.setCreatedAt(new Date());
        return this.productRepositoryImp.add(product)
                .map(p -> ResponseEntity.created(URI.create("/api/products/" + p.getId()))
                .body(p));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/{id}")
    public Mono<ResponseEntity<Product>> get(@PathVariable String id) {
        return this.productRepositoryImp.findById(id)
                .map(p -> ResponseEntity.ok(p))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
