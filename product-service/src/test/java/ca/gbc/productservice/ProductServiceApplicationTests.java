package ca.gbc.productservice;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class ProductServiceApplicationTests {

	@Container
	public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.5");

	@Value("${local.server.port}")
	private int port;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@BeforeEach
	public void setUp() {
		RestAssured.port = port;
	}

	@Test
	void postProductsTest(){
		String requestBody = """
					{
						"name":"iPhone",
						"description":"14 Pro",
						"price":1000
					}
				""";
		RestAssured.given()
				.contentType("application/json")
				.body(requestBody)
				.when()
				.post("/api/product")
				.then()
				.log().all()
				.statusCode(201)
				.body("id", Matchers.notNullValue())
				.body("name", Matchers.equalTo("iPhone"))
				.body("description", Matchers.equalTo("14 Pro"))
				.body("price", Matchers.equalTo(1000));
	}

	@Test
	void getProductsTest(){
		String requestBody = """
					{
						"name":"iPhone",
						"description":"15 Pro",
						"price":1100
					}
				""";
		RestAssured.given()
				.contentType("application/json")
				.body(requestBody)
				.when()
				.post("/api/product")
				.then()
				.log().all()
				.statusCode(201)
				.body("id", Matchers.notNullValue())
				.body("name", Matchers.equalTo("iPhone"))
				.body("description", Matchers.equalTo("15 Pro"))
				.body("price", Matchers.equalTo(1100));

		RestAssured.given()
				.contentType("application/json")
				.when()
				.get("/api/product")
				.then()
				.log().all()
				.statusCode(200)
				.body("size()",Matchers.greaterThan(0))
				.body("[0].name",Matchers.equalTo("iPhone"))
				.body("[0].description",Matchers.equalTo("15 Pro"))
				.body("[0].price",Matchers.equalTo(1100));
	}
}