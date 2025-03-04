package services;


import models.Message;
import repositories.MessageRepository;
import util.IdGenerator;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис для работы с сообщениями.
 * Предоставляет методы для чтения и отправки сообщений.
 */
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private IdGenerator idGenerator;


    /**
     * Конструктор для создания экземпляра сервиса.
     *
     * @param messageRepository репозиторий для работы с сообщениями
     * @throws IllegalArgumentException если messageRepository равен null
     */
    public MessageServiceImpl(MessageRepository messageRepository) {
        if (messageRepository == null) {
            throw new IllegalArgumentException("Репозиторий не может быть null");
        }
        this.messageRepository = messageRepository;
    }


    @Override
    public void sendMessage(Long id, Long senderId, Long receiverId, String content) {
        Message message = new Message(
                id,
                senderId,
                receiverId,
                content,
                LocalDateTime.now()
        );
        messageRepository.save(message);
    }

    @Override
    public List<Message> getMessagesBetweenUsers(Long senderId, Long receiverId) {
        return messageRepository.findMessagesBySenderAndReceiver(senderId, receiverId);
    }

    @Override
    public List<Message> getUserMessages(Long userId) {
        return messageRepository.findMessagesByUser(userId);
    }
}
