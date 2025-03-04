package repositories;


import models.User;
import util.IdGenerator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Реализация репозитория пользователей, основанная на хранении данных в файле.
 * Каждый пользователь сохраняется в файле в виде строки, где поля разделены символом '|'.
 */
public class UsersRepositoryFileBasedImpl implements UsersRepository {

    private final String fileName;
    private final IdGenerator idGenerator;


    /**
     * Функция для преобразования строки из файла в объект User.
     */
    private final Function<String, User> lineToUserFunction = line -> {
        String[] parts = line.split("\\|");
        String id = parts[0];
        String email = parts[1];
        String password = parts[2];
        String name = parts[3];
        String lastName = parts[4];
        String role = parts[5];
        return new User(Long.parseLong(id), email, password, name, lastName, User.Role.valueOf(role));
    };


    /**
     * Конструктор репозитория.
     *
     * @param fileName    имя файла для хранения данных
     * @param idGenerator генератор уникальных идентификаторов
     */
    public UsersRepositoryFileBasedImpl(String fileName, IdGenerator idGenerator) {
        this.fileName = fileName;
        this.idGenerator = idGenerator;
        initializeFile();
    }


    /**
     * Инициализирует файл, если он не существует.
     */
    private void initializeFile() {
        try (Writer writer = new FileWriter(fileName, true)) {
            // Файл создается автоматически, если не существует
        } catch (IOException e) {
            throw new IllegalStateException("Ошибка при инициализации файла: " + e.getMessage(), e);
        }
    }


    @Override
    public void save(User model) {
        if (model == null) {
            throw new IllegalArgumentException("Модель пользователя не может быть null");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            model.setId(idGenerator.nextId());
            String userLine = String.join("|",
                    model.getId().toString(),
                    model.getEmail(),
                    model.getPassword(),
                    model.getName(),
                    model.getLastName(),
                    model.getRole().toString()
            );
            writer.write(userLine + "\n");
        } catch (IOException e) {
            throw new IllegalStateException("Ошибка при сохранении пользователя: " + e.getMessage(), e);
        }
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    User user = lineToUserFunction.apply(line);
                    users.add(user);
                } catch (IllegalArgumentException e) {
                    System.err.println("Ошибка при чтении строки: " + line + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Ошибка при чтении файла: " + e.getMessage(), e);
        }
        return users;
    }

    @Override
    public void deleteById(Long id) {
        List<User> users = new ArrayList<>();
        for (User user : users){
            if (user.getId().equals(id)){
                user.setStatus(User.Status.FIRED);
            }
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return findAll().stream()
                .filter(user -> email.equals(user.getEmail()))
                .findFirst();
    }

    @Override
    public Optional<User> findById(Long id) {
        return findAll().stream().filter(user -> id.equals(user.getId())).findFirst();
    }
}
