package org.vinio.conf;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RMQConfig {
//    передавать названия очередей через переменные окружения
    static final String EXCHANGE_NAME = "testExchange";
    public enum QueueNames {
        FIRST_QUEUE("firstQueue"),
        GRPC_QUEUE("GRPCQueue"),
        AUDIT_QUEUE("AuditQueue"),
        NOTIFICATION_QUEUE("NotificationQueue");
        private final String name;
        QueueNames(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
    }

    @Bean
    public Exchange exchange() {
        return new TopicExchange(EXCHANGE_NAME, false, false);
    }

    // Создание очереди с указанным именем
    @Bean
    public Queue firstQueue() {
        return createQueue(QueueNames.FIRST_QUEUE);
    }

    @Bean
    public Queue grpcQueue() {
        return createQueue(QueueNames.GRPC_QUEUE);
    }

    @Bean
    public Queue auditQueue() {
        return createQueue(QueueNames.AUDIT_QUEUE);
    }

    @Bean
    public Queue notificationQueue() {
        return createQueue(QueueNames.NOTIFICATION_QUEUE);
    }

    private Queue createQueue(QueueNames queueName) {
        return new Queue(queueName.getName(), false);
    }

    // Создание привязок для каждой очереди
    @Bean
    public Binding bindingFirstQueue(Queue firstQueue, Exchange exchange) {
        return createBinding(firstQueue, exchange, "my.key");
    }

    @Bean
    public Binding bindingGrpcQueue(Queue grpcQueue, Exchange exchange) {
        return createBinding(grpcQueue, exchange, "grpc.key");
    }

    @Bean
    public Binding bindingAuditQueue(Queue auditQueue, Exchange exchange) {
        return createBinding(auditQueue, exchange, "audit.key");
    }

    @Bean
    public Binding bindingNotificationQueue(Queue notificationQueue, Exchange exchange) {
        return createBinding(notificationQueue, exchange, "notification.key");
    }

    private Binding createBinding(Queue queue, Exchange exchange, String routingKey) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
    }
}
