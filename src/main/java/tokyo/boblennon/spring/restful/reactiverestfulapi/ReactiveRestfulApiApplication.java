package tokyo.boblennon.spring.restful.reactiverestfulapi;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import reactor.core.publisher.Flux;
import tokyo.boblennon.spring.restful.reactiverestfulapi.domain.category.Category;
import tokyo.boblennon.spring.restful.reactiverestfulapi.domain.product.Product;
import tokyo.boblennon.spring.restful.reactiverestfulapi.infrastructure.category.CategoryRepositoryImp;
import tokyo.boblennon.spring.restful.reactiverestfulapi.infrastructure.product.ProductRepositoryImp;

@SpringBootApplication
public class ReactiveRestfulApiApplication implements CommandLineRunner{

	@Autowired
	private ProductRepositoryImp productRepositoryImp;

	@Autowired
	private CategoryRepositoryImp categoryRepositoryImp;

	@Autowired
	private ReactiveMongoTemplate mongoTemplate;

	private static final Logger log = LoggerFactory.getLogger(ReactiveRestfulApiApplication.class); 
	public static void main(String[] args) {
		SpringApplication.run(ReactiveRestfulApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		mongoTemplate.dropCollection("categories").subscribe();
		mongoTemplate.dropCollection("products").subscribe();
		
		Category fruit = new Category("Fruta");
		Category meat = new Category("Carne");
		Category vegetable = new Category("Verdura");

		Flux.just(fruit, meat, vegetable)
			.flatMap(categoryRepositoryImp::add)
			.thenMany(
				Flux.just(
					new Product("Platano canario", 10.00, fruit),
					new Product("Platano galego", 6.00, meat),
					new Product("Platano barcelones", 10.00, fruit),
					new Product("Platano madrileño", 6.00, vegetable),
					new Product("Platano sevillano", 10.00, vegetable),
					new Product("Platano valenciano", 6.00, meat),
					new Product("Platano murciano", 2.00, fruit)
				)
					.flatMap(p -> {
						p.setCreatedAt(new Date());
						return productRepositoryImp.add(p);
					}))

		
			.subscribe(p -> log.info("Insert: " + p.getId() + " , " + p.getName()));
	}
}
