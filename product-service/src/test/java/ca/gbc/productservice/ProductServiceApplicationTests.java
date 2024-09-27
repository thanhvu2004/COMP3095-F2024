package ca.gbc.productservice;

import ca.gbc.productservice.model.Product;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class ProductServiceApplicationTests {

	// Define the MongoDB container
	@Container
	public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.5");

	// Inject the random port used by the Spring Boot application
	@Value("${local.server.port}")
	private int port;

	// Set dynamic properties for MongoDB TestContainer
	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	// Configure RestAssured before each test
	@BeforeEach
	public void setUp() {
		RestAssured.port = port;
	}

	// Sample Product creation method
	private Product createProduct() {
		return new Product("123", "Test Product", "This is a test product", new BigDecimal("10.99"));
	}

	// POST request integration test for creating a product
	@Test
	void shouldCreateProduct() {
		Product productRequest = createProduct();

		// Perform POST request using RestAssured and verify the response
		given()
				.contentType(ContentType.JSON)
				.body(productRequest)
				.when()
				.post("/api/product")
				.then()
				.statusCode(201) // HTTP 201 Created
				.body("name", is(productRequest.getName()))
				.body("price", is(productRequest.getPrice().floatValue())); // Handling BigDecimal
	}
}