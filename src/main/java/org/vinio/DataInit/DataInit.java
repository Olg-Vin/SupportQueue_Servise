package org.vinio.DataInit;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.vinio.entities.MessageEntity;
import org.vinio.entities.ReplyEntity;
import org.vinio.entities.UserEntity;
import org.vinio.repositories.MessageRepository;
import org.vinio.repositories.ReplyRepository;
import org.vinio.repositories.UserRepository;

import java.util.Date;
import java.util.List;

@Component
public class DataInit implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Override
    public void run(String... args) throws Exception {
        // Создаем тестовых пользователей
        UserEntity user1 = new UserEntity(null, "John Doe", "null@null", null);
        UserEntity user2 = new UserEntity(null, "Jane Smith", "null@null", null);

        userRepository.saveAll(List.of(user1, user2));

        Date date = new Date();

        // Создаем тестовые сообщения для пользователей
        MessageEntity message1 = new MessageEntity(null, user1, "Technical Issue", "Can't log in", 2, "I'm having trouble logging in to my account.", date, "New", null);
        MessageEntity message2 = new MessageEntity(null, user2, "Suggestion", "Add new feature", 2, "I suggest adding a dark mode feature.", date, "New", null);

        messageRepository.saveAll(List.of(message1, message2));

        // Создаем тестовые ответы на сообщения
        ReplyEntity reply1 = new ReplyEntity(null, message1, "Please try resetting your password.", date, "");
        ReplyEntity reply2 = new ReplyEntity(null, message2, "Thank you for your suggestion! We'll consider it.", date, "");

        replyRepository.saveAll(List.of(reply1, reply2));
    }
}
