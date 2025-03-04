package repositories;



import models.Message;
import util.IdGenerator;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Реализация репозитория для работы с сообщениями, основанная на файловой системе.
 * Предоставляет методы для сохранения, поиска и чтения сообщений.
 */
public class MessageRepositoryFileBaseImpl implements MessageRepository {

    private static final Logger logger = Logger.getLogger(MessageRepositoryFileBaseImpl.class.getName());

    private final String fileName;
    private final IdGenerator idGenerator;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Конструктор для создания экземпляра репозитория.
     *
     * @param fileName    имя файла для хранения данных
     * @param idGenerator генератор уникальных идентификаторов
     * @throws IllegalArgumentException если fileName или idGenerator равен null
     */
    public MessageRepositoryFileBaseImpl(String fileName, IdGenerator idGenerator) {
        if (fileName == null || idGenerator == null) {
            throw new IllegalArgumentException("Параметры fileName и idGenerator не могут быть null");
        }
        this.fileName = fileName;
        this.idGenerator = idGenerator;
    }

    /**
     * Функция для преобразования строки из файла в объект {@link Message}.
     * Формат строки: id|messageFrom|dispatchTime|message|messageTo
     *
     * @param line строка из файла
     * @return объект {@link Message}
     * @throws IllegalArgumentException если строка имеет некорректный формат
     */
    private final Function<String, Message> lineToMessageFunction = line -> {
        String[] parts = line.split("\\|");
        if (parts.length < 6) {
            throw new IllegalArgumentException("Некорректный формат строки: " + line);
        }
        Long id = Long.parseLong(parts[0]);
        Long senderId = Long.parseLong(parts[1]);
        Long receiverId = Long.parseLong(parts[2]);
        String content = parts[3];
        LocalDateTime dispatchTime = LocalDateTime.parse(parts[4]);
        return new Message(id, senderId, receiverId, content, dispatchTime);
    };

    /**
     * Сохраняет сообщение в файл.
     *
     * @param model объект {@link Message}, который необходимо сохранить
     * @throws IllegalArgumentException если model равен null
     * @throws RuntimeException         если произошла ошибка при записи в файл
     */
    @Override
    public void save(Message model) {
        if (model == null) {
            throw new IllegalArgumentException("Модель сообщения не может быть null");
        }

        model.setId(idGenerator.nextId());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            String dispatchTime = model.getTimestamp().format(dateFormatter);
            writer.write(String.format("%d|%s|%s|%s|%s|%s\n",
                    model.getId(),
                    model.getSenderId(),
                    model.getReceiverId(),
                    model.getContent(),
                    model.getTimestamp(),
                    dispatchTime));
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении сообщения в файл: " + e.getMessage(), e);
        }
    }

    /**
     * Возвращает список всех сообщений из файла.
     *
     * @return список всех сообщений
     * @throws IllegalStateException если произошла ошибка при чтении файла
     */
    @Override
    public List<Message> findAll() {
        List<Message> messages = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    Message message = lineToMessageFunction.apply(line);
                    messages.add(message);
                } catch (IllegalArgumentException e) {
                    logger.warning("Ошибка при чтении строки: " + line + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Ошибка при чтении файла: " + e.getMessage(), e);
        }
        return messages;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<Message> findMessagesBySenderAndReceiver(Long senderId, Long receiverId) {
        return findAll().stream()
                .filter(message -> message.getSenderId().equals(senderId) && message.getReceiverId().equals(receiverId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> findMessagesByUser(Long userId) {
        return findAll().stream()
                .filter(message -> message.getSenderId().equals(userId) || message.getReceiverId().equals(userId))
                .collect(Collectors.toList());
    }
}
