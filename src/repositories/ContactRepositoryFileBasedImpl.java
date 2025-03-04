package repositories;


import models.Contact;
import util.IdGenerator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Класс ContactRepositoryFileBasedImpl реализует интерфейс ContactRepository
 * и предоставляет функциональность для работы с контактами, хранящимися в файле.
 * Класс позволяет добавлять, удалять и искать контакты по идентификатору клиента.
 */
public class ContactRepositoryFileBasedImpl implements ContactRepository {


    /**
     * Имя файла, в котором хранятся данные о контактах.
     */
    private final String fileName;

    /**
     * Генератор уникальных идентификаторов для новых контактов.
     */
    private final IdGenerator idGenerator;

    private static final Logger logger = Logger.getLogger(ContactRepositoryFileBasedImpl.class.getName());

    /**
     * Конструктор, который принимает имя файла и генератор идентификаторов.
     *
     * @param fileName    имя файла для хранения данных
     * @param idGenerator генератор уникальных идентификаторов
     */
    public ContactRepositoryFileBasedImpl(String fileName, IdGenerator idGenerator) {
        this.fileName = fileName;
        this.idGenerator = idGenerator;
    }

    /**
     * Функция, которая преобразует строку из файла в объект класса Contact.
     * Формат строки: id|clientId|name|email|phone|position
     */
    private final Function<String, Contact> lineToContactFunction = line -> {
        String[] parts = line.split("\\|");
        if (parts.length < 6) {
            throw new IllegalArgumentException("Некорректный формат строки: " + line);
        }
        return new Contact(
                Long.parseLong(parts[0]),
                Long.parseLong(parts[1]),
                parts[2],
                parts[3],
                parts[4],
                parts[5]
        );
    };

    /**
     * Добавляет информацию о новом контакте в файл.
     * Перед добавлением контакту присваивается уникальный идентификатор.
     *
     * @param model объект класса Contact, который необходимо добавить
     * @throws RuntimeException если произошла ошибка при записи данных в файл
     */
    @Override
    public void addingInformation(Contact model) {
        if (model == null) {
            throw new IllegalArgumentException("Модель контакта не может быть null");
        }
        model.setId(idGenerator.nextId());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(String.format("%d|%d|%s|%s|%s|%s\n",
                    model.getId(),
                    model.getClientId(),
                    model.getName(),
                    model.getEmail(),
                    model.getPhone(),
                    model.getPosition()));
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении данных контакта в файл: " + e.getMessage(), e);
        }
    }

    /**
     * Удаляет указанное количество контактов, связанных с указанным идентификатором клиента.
     *
     * @param contactClientId    идентификатор клиента, для которого нужно удалить контакты
     * @param countDeleteContact количество контактов для удаления
     * @throws RuntimeException если произошла ошибка при удалении данных из файла
     */
    @Override
    public void deleteInformation(Long contactClientId, int countDeleteContact) {
        if (contactClientId == null || countDeleteContact < 0) {
            throw new IllegalArgumentException("Идентификатор клиента и количество контактов должны быть корректными");
        }

        try {
            List<String> lines = Files.readAllLines(Path.of(fileName));
            List<String> updatedLines = new ArrayList<>();

            int deletedCount = 0;
            for (String line : lines) {
                Contact contact = lineToContactFunction.apply(line);
                if (contact.getClientId().equals(contactClientId) && deletedCount < countDeleteContact) {
                    deletedCount++;
                } else {
                    updatedLines.add(line);
                }
            }

            Files.write(Path.of(fileName), updatedLines);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при удалении контактов из файла: " + e.getMessage(), e);
        }
    }

    /**
     * Возвращает список всех контактов, связанных с указанным идентификатором клиента.
     *
     * @param contactClientId идентификатор клиента, для которого нужно найти контакты
     * @return список контактов, связанных с указанным клиентом
     * @throws IllegalStateException если произошла ошибка при чтении данных из файла
     */
    @Override
    public List<Contact> findAllContactByClient(Long contactClientId) {
        if (contactClientId == null) {
            throw new IllegalArgumentException("Идентификатор клиента не может быть null");
        }
        return findAll().stream().filter(contact ->
                contact.getClientId().equals(contactClientId)).collect(Collectors.toList());
    }

    private List<Contact> findAll() {
        List<Contact> contacts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    Contact contact = lineToContactFunction.apply(line);
                    contacts.add(contact);
                } catch (IllegalArgumentException e) {
                    logger.warning("Ошибка при чтении строки: " + line + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Ошибка при чтении файла: " + e.getMessage(), e);
        }
        return contacts;
    }
}
