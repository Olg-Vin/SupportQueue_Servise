package org.vinio.controllers.RabbitMQ;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Класс-контроллер для проверки работоспособности очередей
 */
@RestController
@RequestMapping("/send")
public class SendController {
    private final Sender sender;

    @Autowired
    public SendController(Sender sender) {
        this.sender = sender;
    }

    @PostMapping("/{queueName}")
    public ResponseEntity<String> sendMessage(@PathVariable String queueName, @RequestBody String message) {
        sender.sendMessage(queueName, message);
        return ResponseEntity.ok().body("Message sent to " + queueName);
    }
}
