package tokyo.boblennon.spring.restful.reactiverestfulapi.controller;

import java.io.File;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tokyo.boblennon.spring.restful.reactiverestfulapi.domain.product.Product;
import tokyo.boblennon.spring.restful.reactiverestfulapi.infrastructure.product.ProductRepositoryImp;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Value("${config.upload.path}")
    private String path;

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
    public Mono<ResponseEntity<Map<String, Object>>> add(@Valid @RequestBody Mono<Product> monoProduct){
        Map<String, Object> response = new HashMap<>();

        return monoProduct.flatMap(product -> {
            if(product.getCreatedAt() == null)
            product.setCreatedAt(new Date());

            return this.productRepositoryImp.add(product)
                .map(p -> {
                    response.put("product", p);
                    return ResponseEntity.created(URI.create("/api/products/" + p.getId()))
                            .body(response);

                });
        })
        .onErrorResume(t -> {
            return Mono.just(t).cast(WebExchangeBindException.class)
                    .flatMap(ex -> Mono.just(ex.getFieldErrors()))
                    .flatMapMany(Flux::fromIterable)
                    .map(field -> "The field " + field.getField() + " " + field.getDefaultMessage())
                    .collectList()
                    .flatMap(list -> {
                        response.put("errors", list);
                        return Mono.just(ResponseEntity.badRequest().body(response));
                    });
        });
        
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Product>> get(@PathVariable String id) {
        return this.productRepositoryImp.findById(id)
                .map(p -> ResponseEntity.ok(p))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Product>> update(@RequestBody Product product,@PathVariable String id){
        return this.productRepositoryImp.findById(id).flatMap(p -> {
            p.setName(product.getName());
            p.setPrice(product.getPrice());
            p.setCategory(product.getCategory());
            return this.productRepositoryImp.add(p);
        })
        .map(p -> ResponseEntity.created(URI.create("/api/products" + p.getId())).body(p))
        .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id){
        return this.productRepositoryImp.findById(id).flatMap(p -> {
            return this.productRepositoryImp.delete(p)
                    .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
        }) 
        .defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/upload/{id}")
    public Mono<ResponseEntity<Product>> upload(@PathVariable String id, @RequestPart FilePart file){
        return this.productRepositoryImp.findById(id).flatMap(p -> {
            p.setPicture(UUID.randomUUID().toString() + file.filename()
            .replace(" ", "-"));
            return file.transferTo(new File(path + p.getPicture()))
                    .then(this.productRepositoryImp.add(p));
        })
        .map(p -> ResponseEntity.ok(p))
        .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    //? We are sending the data as ContentType::Form-Data instead of JSON
    @PostMapping(path = "/new", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Product>> addWithPicture(Product product, @RequestPart FilePart file){
        if(product.getCreatedAt() == null)
            product.setCreatedAt(new Date());
        product.setPicture(UUID.randomUUID().toString() + file.filename()
        .replace(" ", "-"));
        
        return file.transferTo(new File(path + product.getPicture()))
                .then(this.productRepositoryImp.add(product))
                .map(p -> ResponseEntity.created(URI.create("/api/products/" + p.getId()))
                .body(p));
    }
}
