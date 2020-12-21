package com.gezanoletti.demo.Testcontainers;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest
public class RabbitMqTest {

    private static final String ROUTING_KEY = "routing_key";

    @Container
    private static GenericContainer rabbitmq = new GenericContainer(DockerImageName.parse("rabbitmq:3-management"))
            .withExposedPorts(5672);

    // https://stackoverflow.com/questions/62703535/junit-5-with-test-application-properties
    // https://stackoverflow.com/questions/55975798/how-to-overwrite-ports-defined-in-application-properties-in-integration-tests-af
    @DynamicPropertySource
    static void rabbitMqProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", rabbitmq::getHost);
        registry.add("spring.rabbitmq.port", rabbitmq::getFirstMappedPort);
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testRabbitMqIsRunning() {
        assertTrue(rabbitmq.isRunning());
    }

    @Test
    public void testSendMessageToRabbitMq() {
        final PersonDto dto = new PersonDto(1L, "John", "Lock", 30);
        rabbitTemplate.convertAndSend(ROUTING_KEY, dto);
    }

}
