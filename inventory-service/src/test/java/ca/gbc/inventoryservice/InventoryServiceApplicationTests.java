package ca.gbc.inventoryservice;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InventoryServiceApplicationTests {

	private static final PostgreSQLContainer<?> postgreSQLContainer =
			new PostgreSQLContainer<>("postgres:15-alpine")
					.withDatabaseName("testdb")
					.withUsername("user")
					.withPassword("password");

	@LocalServerPort
	private Integer port;

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@BeforeAll
	static void setUp() {
		postgreSQLContainer.start();
	}

	// Dynamically configure the database connection URL for Spring Boot
	@DynamicPropertySource
	static void dynamicProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
		registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
		registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
	}

	@Test
	void shouldCheckIfItemIsInStock() {
		String skuCode = "SKU001";
		int quantity = 5;

		// Create the fake table and insert fake data if necessary
		jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS inventory (skuCode VARCHAR(50), quantity INT, available BOOLEAN)");
		jdbcTemplate.execute("INSERT INTO inventory (skuCode, quantity, available) VALUES ('SKU001', 100, true)");

		// Perform the test using WebTestClient
		webTestClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/api/inventory")
						.queryParam("skuCode", skuCode)
						.queryParam("quantity", quantity)
						.build())
				.exchange()
				.expectStatus().isOk()
				.expectBody(Boolean.class)
				.value(isInStock -> assertThat(isInStock).isTrue());
	}
}