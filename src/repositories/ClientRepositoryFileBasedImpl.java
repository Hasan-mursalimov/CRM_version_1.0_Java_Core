package repositories;

import models.Client;
import models.Contact;
import models.Task;
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
 * Реализация репозитория для работы с клиентами, основанная на файловой системе.
 */
public class ClientRepositoryFileBasedImpl implements ClientRepository {
    private final String fileName;
    private final IdGenerator idGenerator;

    private static final Logger logger = Logger.getLogger(TaskRepositoryFileBasedImpl.class.getName());

    /**
     * Функция для преобразования строки из файла в объект {@link Client}.
     */
    private final Function<String, Client> lineToClientFunction = line -> {
        String [] parts = line.split("\\|");
        // Проверка на корректное количество частей
        if (parts.length < 7) {
            throw new IllegalArgumentException("Некорректный формат строки: " + line);
        }

        // Парсинг данных
        Long id = Long.parseLong(parts[0]);
        Long userId = Long.parseLong(parts[1]);
        String name = parts[2];
        String email = parts[3];
        String phone = parts[4];
        String address = parts[5];
        Client.Status status = Client.Status.valueOf(parts[6]);

        // Парсинг задач (если они есть)
        List<Task> tasks = new ArrayList<>();
        if (parts.length > 7) {
            String[] taskIds = parts[7].split(",");
            for (String taskId : taskIds) {
                tasks.add(new Task(Long.parseLong(taskId)));
            }
        }

        // Парсинг контактов (если они есть)
        List<Contact> contacts = new ArrayList<>();
        if (parts.length > 8) {
            String[] contactIds = parts[8].split(",");
            for (String contactId : contactIds) {
                contacts.add(new Contact(Long.parseLong(contactId)));
            }
        }

        // Создание объекта Client
        return new Client(id, userId, name, email, phone, address, status, tasks, contacts);
    };

    /**
     * Конструктор для создания экземпляра репозитория с указанием имени файла и генератора идентификаторов.
     *
     * @param fileName    имя файла, в котором хранятся данные о клиентах
     * @param idGenerator генератор уникальных идентификаторов для новых клиентов
     * @throws NullPointerException если любой из параметров равен null
     */
    public ClientRepositoryFileBasedImpl(String fileName, IdGenerator idGenerator) {
        if (fileName == null || idGenerator == null) {
            throw new NullPointerException("Параметры fileName и idGenerator не могут быть null");
        }
        this.fileName = fileName;
        this.idGenerator = idGenerator;
    }


    /**
     * Сохранение модели клиента в файл.
     *
     * @param model модель клиента, которую нужно сохранить
     * @throws UncheckedIOException если возникла ошибка ввода-вывода при записи в файл
     */
    @Override
    public void save(Client model) {
        if (model == null) {
            throw new IllegalArgumentException("Модель клиента не может быть null");
        }

        model.setId(idGenerator.nextId());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(String.format("%d|%d|%s|%s|%s|%s|%s\n",
                    model.getId(),
                    model.getUserId(),
                    model.getName(),
                    model.getEmail(),
                    model.getPhone(),
                    model.getAddress(),
                    model.getStatus()));
        } catch (IOException e) {
            throw new UncheckedIOException("Ошибка при сохранении клиента в файл: " + e.getMessage(), e);
        }
    }


    /**
     * Получение списка всех клиентов из файла.
     *
     * @return список всех клиентов
     * @throws IllegalStateException если произошла ошибка при чтении файла
     */
    @Override
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    Client client = lineToClientFunction.apply(line);
                    clients.add(client);
                } catch (IllegalArgumentException e) {
                    logger.warning("Ошибка при чтении строки: " + line + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Ошибка при чтении файла: " + e.getMessage(), e);
        }
        return clients;
    }


    /**
     * Находит всех клиентов, связанных с указанным пользователем.
     *
     * @param idUser идентификатор пользователя, для которого нужно найти клиентов
     * @return список клиентов, принадлежащих данному пользователю
     */
    @Override
    public List<Client> findAllClientToUser(Long idUser) {
        if (idUser == null) {
            throw new IllegalArgumentException("Идентификатор пользователя не может быть null");
        }

        return findAll().stream()
                .filter(client -> client.getUserId().equals(idUser))
                .collect(Collectors.toList());
    }

    /**
     * Обновляет статус указанного клиента.
     *
     * @param updatedClient клиент, статус которого нужно обновить
     * @param newStatus     новый статус клиента
     * @throws RuntimeException если клиент с указанным ID не был найден
     * @throws RuntimeException если произошла ошибка при обновлении файла
     */
    @Override
    public void updateClientStatus(Client updatedClient, Client.Status newStatus) {
        if (updatedClient == null || newStatus == null) {
            throw new IllegalArgumentException("Клиент и новый статус не могут быть null");
        }

        try {
            List<String> lines = Files.readAllLines(Path.of(fileName));
            boolean found = false;

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split("\\|");
                long currentId = Long.parseLong(parts[0]);

                if (currentId == updatedClient.getId()) {
                    String newLine = String.format("%d|%d|%s|%s|%s|%s|%s",
                            updatedClient.getId(),
                            updatedClient.getUserId(),
                            updatedClient.getName(),
                            updatedClient.getEmail(),
                            updatedClient.getPhone(),
                            updatedClient.getAddress(),
                            newStatus.name());

                    lines.set(i, newLine);
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new RuntimeException("Клиент с таким ID не найден.");
            }
            Files.writeString(Path.of(fileName), String.join(System.lineSeparator(), lines));

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при обновлении клиента в файле: " + e.getMessage(), e);
        }
    }

    /**
     * Осуществляет поиск клиента по его уникальному идентификатору.
     *
     * @param id уникальный идентификатор клиента
     * @return найденный клиент или null, если клиент не найден
     */
    @Override
    public Client searchClientById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Идентификатор клиента не может быть null");
        }

        return findAll().stream()
                .filter(client -> client.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Выполняет поиск клиентов по заданному критерию.
     *
     * @param search Критерий поиска. Может быть id, именем, email или номером телефона клиента.
     * @return Список клиентов, соответствующих критерию поиска.
     */
    @Override
    public List<Client> searchClient(String search) {
        if (search == null || search.trim().isEmpty()) {
            throw new IllegalArgumentException("Критерий поиска не может быть null или пустым");
        }

        return findAll().stream()
                .filter(client -> matchesSearchCriteria(client, search))
                .collect(Collectors.toList());
    }

    /**
     * Обновляет информацию о клиенте.
     *
     * @param updatedClientId идентификатор клиента, информацию о котором нужно обновить
     * @param whatToChange    индекс поля, которое нужно изменить
     * @param newMeaning      новое значение поля
     * @throws RuntimeException если клиент с указанным ID не был найден
     * @throws RuntimeException если произошла ошибка при обновлении файла
     */
    @Override
    public void updateClientInfo(Long updatedClientId, int whatToChange, String newMeaning) {
        if (updatedClientId == null || newMeaning == null) {
            throw new IllegalArgumentException("Идентификатор клиента и новое значение не могут быть null");
        }

        try {
            List<String> lines = Files.readAllLines(Path.of(fileName));
            boolean found = false;

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split("\\|");
                long currentId = Long.parseLong(parts[0]);

                if (currentId == updatedClientId) {
                    parts[whatToChange] = newMeaning;
                    String newLine = String.join("|", parts);
                    lines.set(i, newLine);
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new RuntimeException("Клиент с таким ID не найден.");
            }
            Files.writeString(Path.of(fileName), String.join(System.lineSeparator(), lines));

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при обновлении клиента в файле: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteById(Long id) {
        List<Client> clients = findAll();
        for (Client client : clients){
            if (client.getId().equals(id)){
                client.setStatus(Client.Status.DELETE);
            }
        }
    }


    /**
     * Проверяет, соответствует ли клиент критерию поиска.
     *
     * @param client клиент для проверки
     * @param search критерий поиска
     * @return true, если клиент соответствует критерию, иначе false
     */
    private boolean matchesSearchCriteria(Client client, String search) {
        return client.getName().equals(search) || client.getEmail().equals(search) || client.getPhone().equals(search);
    }
}


