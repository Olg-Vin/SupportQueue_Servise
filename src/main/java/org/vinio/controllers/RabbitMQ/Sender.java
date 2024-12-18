package org.vinio.controllers.RabbitMQ;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.vinio.exceptions.InvalidArgumentException;

@Component
public class Sender {
    private final RabbitTemplate rabbitTemplate;
    private static final String EXCHANGE_NAME = "defaultExchange";

    public Sender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String queueName, String message) {
        String routingKey = getRoutingKeyForQueue(queueName);
        if (routingKey == null) {
            throw new InvalidArgumentException("Invalid queue name");
        }
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, routingKey, message);
    }

    private String getRoutingKeyForQueue(String queueName) {
        return switch (queueName) {
            case "GRPCQueue" -> "grpc.key";
            case "AuditQueue" -> "audit.key";
            default -> null;
        };
    }
}
