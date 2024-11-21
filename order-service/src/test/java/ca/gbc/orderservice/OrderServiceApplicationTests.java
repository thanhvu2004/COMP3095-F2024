package ca.gbc.orderservice;

import ca.gbc.orderservice.stub.InventoryClientStub;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderServiceApplicationTests {

	@ServiceConnection
	private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine");

	@LocalServerPort
	private Integer port;

	private static WireMockServer wireMockServer;

	@BeforeAll
	static void startWireMock() {
		wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(8080));
		wireMockServer.start();
	}

	@AfterAll
	static void stopWireMock() {
		if (wireMockServer != null) {
			wireMockServer.stop();
		}
	}

	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	static {
		postgreSQLContainer.start();
	}

	@Test
	void shouldSubmitOrder() {
		String submitOrderJson = """
            {
                "skuCode": "SKU001",
                "price": 100.00,
                "quantity": 10
            }
            """;

		InventoryClientStub.stubInventoryCall("SKU001", 10);

		var response = RestAssured.given()
				.contentType("application/json")
				.body(submitOrderJson)
				.when()
				.post("/api/order")
				.then()
				.log().all()
				.extract()
				.response();

		int statusCode = response.getStatusCode();
		String responseBodyString = response.getBody().asString();

		System.out.println("Status Code: " + statusCode);
		System.out.println("Response Body: " + responseBodyString);

		assertThat(statusCode).isEqualTo(201);
		assertThat(responseBodyString).contains("Order placed successfully");
	}
}