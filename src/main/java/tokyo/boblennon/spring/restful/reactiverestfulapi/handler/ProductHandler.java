package tokyo.boblennon.spring.restful.reactiverestfulapi.handler;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.io.File;
import java.net.URI;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;
import tokyo.boblennon.spring.restful.reactiverestfulapi.domain.category.Category;
import tokyo.boblennon.spring.restful.reactiverestfulapi.domain.product.Product;
import tokyo.boblennon.spring.restful.reactiverestfulapi.infrastructure.product.ProductRepositoryImp;

@Component
public class ProductHandler {
    
    @Value("${config.upload.path}")
    private String path;

    private final ProductRepositoryImp productRepositoryImp;

    @Autowired
    public ProductHandler(final ProductRepositoryImp productRepositoryImp){
        this.productRepositoryImp = productRepositoryImp;
    }

    public Mono<ServerResponse> getAll(ServerRequest request){
        return ServerResponse.ok()

        .contentType(APPLICATION_JSON)
        .body(this.productRepositoryImp.getAll(), Product.class);
    }

    public Mono<ServerResponse>  get(ServerRequest request){
        String id = request.pathVariable("id");

        return this.productRepositoryImp.findById(id).flatMap(p -> ServerResponse
                .ok()
                .contentType(APPLICATION_JSON)
                .bodyValue(p))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse>  add(ServerRequest request){
        Mono<Product> product = request.bodyToMono(Product.class);

        return product.flatMap((p -> {
            if(p.getCreatedAt() == null)
                p.setCreatedAt(new Date());
            return this.productRepositoryImp.add(p);
        }))
        .flatMap(p -> ServerResponse.created(URI
                    .create("/api/v2/product/" + p.getId()))
                    .contentType(APPLICATION_JSON)
                    .bodyValue(p)
        );
    }

    public Mono<ServerResponse> update(ServerRequest request){
        Mono<Product> product = request.bodyToMono(Product.class);
        String id = request.pathVariable("id");

        Mono<Product> productDB = this.productRepositoryImp.findById(id);

        return productDB.zipWith(product, (db, req) -> {
            db.setName(req.getName());
            db.setPrice(req.getPrice());
            db.setCategory(req.getCategory());
            return db;
        })
        .flatMap(p -> ServerResponse.created(URI
                    .create("/api/v2/products/" + p.getId()))
                    .body(this.productRepositoryImp.add(p), Product.class))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest request){
        String id = request.pathVariable("id");

        return this.productRepositoryImp.findById(id).flatMap(p -> 
            this.productRepositoryImp.delete(p).then(ServerResponse.noContent().build()))
            .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> upload(ServerRequest request){
        String id = request.pathVariable("id");

        return request.multipartData().map(multipart -> multipart.toSingleValueMap().get("file"))
                .cast(FilePart.class)
                .flatMap(file -> this.productRepositoryImp.findById(id).flatMap(p -> {
                    p.setPicture(UUID.randomUUID().toString() + "-" + file.filename().replace(" ", "-"));
                    return file.transferTo(new File(path + p.getPicture()))
                            .then(this.productRepositoryImp.add(p));
                }))
                .flatMap(p -> ServerResponse.created(URI
                    .create("/api/v2/products/" + p.getId()))
                    .contentType(APPLICATION_JSON)
                    .bodyValue(p))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> addWithPicture(ServerRequest request){
        Mono<Product> product = this.multipartToProduct(request);

        return request.multipartData().map(multipart -> multipart.toSingleValueMap().get("file"))
                .cast(FilePart.class)
                .flatMap(file -> product.flatMap(p -> {
                    p.setPicture(UUID.randomUUID().toString() + "-" + file.filename().replace(" ", "-"));
                    p.setCreatedAt(new Date());
                    return file.transferTo(new File(path + p.getPicture()))
                            .then(this.productRepositoryImp.add(p));
                }))
                .flatMap(p -> ServerResponse.created(URI
                    .create("/api/v2/products/" + p.getId()))
                    .contentType(APPLICATION_JSON)
                    .bodyValue(p));
    }

    private Mono<Product> multipartToProduct(ServerRequest request){
        Mono<Product> product = request.multipartData().map(multipart -> {
            FormFieldPart name = (FormFieldPart) multipart.toSingleValueMap().get("name");
            FormFieldPart price = (FormFieldPart) multipart.toSingleValueMap().get("price");
            FormFieldPart categoryId = (FormFieldPart) multipart.toSingleValueMap().get("category.id");
            FormFieldPart categoryName = (FormFieldPart) multipart.toSingleValueMap().get("category.name");
            Category category = new Category(categoryName.value());
            category.setId(categoryId.value());
            return new Product(name.value(), Double.parseDouble(price.value()), category);
        });
        return product;
    }

}
