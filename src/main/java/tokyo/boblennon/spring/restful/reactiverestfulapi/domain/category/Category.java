package tokyo.boblennon.spring.restful.reactiverestfulapi.domain.category;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "categories")
public @Getter @Setter @NoArgsConstructor class Category {

    @Id
    @NotEmpty
    private String id;

    @NotEmpty
    private String name;

    public Category(String name) {
        this.name = name;
    }

}
