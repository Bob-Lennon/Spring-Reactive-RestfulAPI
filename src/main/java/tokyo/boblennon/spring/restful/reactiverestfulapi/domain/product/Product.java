package tokyo.boblennon.spring.restful.reactiverestfulapi.domain.product;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tokyo.boblennon.spring.restful.reactiverestfulapi.domain.categories.Category;

@Document(collection = "products")
public @Getter @Setter @NoArgsConstructor class Product {

    @Id
    private String id;

    @NotEmpty
    private String name;

    @NotNull
    private Double price;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    @Valid
    private Category category;

    private String picture;

    public Product(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public Product(String name, Double price, Category category) {
        this(name, price);
        this.category = category;
    }

}
