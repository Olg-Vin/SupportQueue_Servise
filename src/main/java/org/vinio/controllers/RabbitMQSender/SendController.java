package org.vinio.controllers.RabbitMQSender;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/send")
public class SendController {
    private final RabbitTemplate rabbitTemplate;
    private static final String EXCHANGE_NAME = "testExchange";

    public SendController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/{queueName}")
    public ResponseEntity<String> sendMessage(@PathVariable String queueName, @RequestBody String message) {
        String routingKey = getRoutingKeyForQueue(queueName);
        if (routingKey == null) {
            return ResponseEntity.badRequest().body("Invalid queue name");
        }
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, routingKey, message);
        return ResponseEntity.ok().body("Message sent to " + queueName);
    }

    private String getRoutingKeyForQueue(String queueName) {
        return switch (queueName) {
            case "firstQueue" -> "my.key";
            case "GRPCQueue" -> "grpc.key";
            case "AuditQueue" -> "audit.key";
            case "NotificationQueue" -> "notification.key";
            default -> null;
        };
    }
}
