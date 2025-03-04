package services;


import dto.UserDto;
import models.User;
import repositories.UsersRepository;
import util.MailUtil;
import util.ValidationUtil;

import java.util.List;
import java.util.Optional;

import static dto.UserDto.from;


/**
 * Сервис для управления пользователями.
 * Предоставляет методы для регистрации, входа и получения списка пользователей.
 */
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;
    private final MailUtil mailUtil;

    /**
     * Конструктор сервиса.
     *
     * @param usersRepository репозиторий для работы с пользователями
     * @param mailUtil        утилита для отправки электронной почты
     */
    public UsersServiceImpl(UsersRepository usersRepository, MailUtil mailUtil) {
        this.usersRepository = usersRepository;
        this.mailUtil = mailUtil;
    }

    @Override
    public void signUp(Long id, String login, String password, String name, String lastName, User.Role role) {
        ValidationUtil.checkNotNull(id, "Идентификатор id не может быть null");
        ValidationUtil.checkNotEmptyEmail(login, "Логин не может быть null или не правильно введен");
        ValidationUtil.checkNotEmpty(password, "Пароль не может быть null");
        ValidationUtil.checkNotEmpty(name, "Имя не может быть null");
        ValidationUtil.checkNotEmpty(lastName, "Фамилия не может быть null");
        ValidationUtil.checkNotNull(role, "Должность не может быть null");
        // Создаем модель пользователя
        User user = new User(id, login, password, name, lastName, role);
        usersRepository.save(user);
        // Отправляем письмо с подтверждением регистрации
        mailUtil.sendMail(login, "Вы были успешно зарегистрированы, ваш временный пароль: " + password);
    }

    /**
     * Проверяет, что логин и пароль не являются null или пустыми, а также валидность email.
     *
     * @param login    логин (email) пользователя
     * @param password пароль пользователя
     * @throws IllegalArgumentException если логин или пароль некорректны
     */


    @Override
    public boolean signIn(String login, String password) {
        ValidationUtil.checkNotEmpty(login, "Логин не может быть null");
        ValidationUtil.checkNotEmpty(password, "Пароль не может быть null");
        Optional<User> userOptional = usersRepository.findByEmail(login);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getPassword().equals(password); // Сравнение паролей (в реальном проекте используйте хэширование)
        }
        return false;
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> users = usersRepository.findAll();
        return from(users);
    }

    @Override
    public void deleteUser(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Идентификатор пользователя не может быть null");
        }
        // Логика удаления пользователя
    }

    /**
     * Проверяет, соответствует ли email формату.
     *
     * @param email email для проверки
     * @return true, если email корректен, иначе false
     */
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}
