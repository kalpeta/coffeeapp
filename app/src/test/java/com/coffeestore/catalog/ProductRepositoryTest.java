package com.coffeestore.catalog;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(ProductRepositoryTest.class);

    @Container
    static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:16.3-alpine")
                    .withDatabaseName("coffeestore")
                    .withUsername("cs_user")
                    .withPassword("cs_pass");

    @BeforeAll
    static void attachLogs() {
        POSTGRES.followOutput(new Slf4jLogConsumer(log));
    }

    @DynamicPropertySource
    static void registerDataSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        // Keep Flyway enabled (from application-test.yml). No need to set driver: autoconfig picks it.
    }

    @Autowired
    ProductJpaRepository repo;

    @Test
    @DisplayName("saving and retrieving a product works via JPA + Testcontainers Postgres")
    void saveAndReadBack() {
        ProductEntity saved = repo.save(new ProductEntity("Mocha", "Chocolate + espresso", 500, 10));
        assertThat(saved.getId()).isNotNull();

        var found = repo.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Mocha");
        assertThat(repo.existsByName("Mocha")).isTrue();
    }
}
