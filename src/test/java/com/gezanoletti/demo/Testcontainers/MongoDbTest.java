package com.gezanoletti.demo.Testcontainers;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class MongoDbTest {

    @Container
    private static GenericContainer mongodb = new GenericContainer(DockerImageName.parse("bitnami/mongodb:4.0"))
            .withExposedPorts(27017);

    @DynamicPropertySource
    static void mongodbProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", mongodb::getHost);
        registry.add("spring.rabbitmq.port", mongodb::getFirstMappedPort);
    }

    @Test
    public void testMongoDbIsRunning() {
        assertTrue(mongodb.isRunning());
    }
}
