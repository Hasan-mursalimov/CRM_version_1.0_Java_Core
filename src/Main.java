

import dto.ClientDto;
import dto.UserDto;
import models.*;
import repositories.*;
import services.*;
import template.DocumentGenerator;
import util.IdGenerators;
import util.MailUtil;
import util.MailUtilMockImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final Scanner scanner = new Scanner(System.in);

    private static final ExecutorService executor = Executors.newFixedThreadPool(4);
    // Репозитории и сервисы
    private static final UsersRepository usersRepository = new UsersRepositoryFileBasedImpl("users.txt",
            IdGenerators.createGenerator("users_id.txt"));
    private static final ClientRepository clientRepository = new ClientRepositoryFileBasedImpl("client.txt",
            IdGenerators.createGenerator("client_id.txt"));
    private static final ContactRepository contactRepository = new ContactRepositoryFileBasedImpl("contact.txt",
            IdGenerators.createGenerator("contact_id.txt"));
    private static final DealRepository dealRepository = new DealRepositoryFileBasedImpl("deal.txt",
            IdGenerators.createGenerator("deal_id.txt"));
    private static final TaskRepository taskRepository = new TaskRepositoryFileBasedImpl("task.txt",
            IdGenerators.createGenerator("task_id.txt"));

    private static final MessageRepository messageRepository = new MessageRepositoryFileBaseImpl("message.tst",
            IdGenerators.createGenerator("message_id.txt"));
    private static final DocumentGenerator documentGenerator = new DocumentGenerator("sales_contract.txt");
    private static final MailUtil mailUtil = new MailUtilMockImpl();
    private static final UsersService usersService = new UsersServiceImpl(usersRepository, mailUtil);
    private static final ClientService clientService = new ClientServiceImpl(clientRepository, documentGenerator);
    private static final ContactService contactService = new ContactServiceImpl(contactRepository);
    private static final DealService dealService = new DealServiceImpl(dealRepository);
    private static final TaskService taskService = new TaskServiceImpl(taskRepository);

    private static final MessageService messageService = new MessageServiceImpl(messageRepository);
    private static boolean isAuthenticated = false;

    private static Long currentUserId = null; //ID текущего пользователя

    public static void main(String[] args) {
        logger.info("Программа запущена.");

        while (true) {
            printMenu();
            int command = readIntInput("Введите команду: ");

            try {
                switch (command) {
                    case 1 -> registerUser();
                    case 2 -> authenticateUser();
                    case 3 -> findAllUsers();
                    case 4 -> addClient();
                    case 5 -> findAllClients();
                    case 6 -> findClientsByManager();
                    case 7 -> deleteClient();
                    case 8 -> searchClient();
                    case 9 -> updateClientInfo();
                    case 10 -> addContact();
                    case 11 -> showClientDetails();
                    case 12 -> addDeal();
                    case 13 -> findAllDeals();
                    case 14 -> deleteDeal();
                    case 15 -> addTask();
                    case 16 -> findAllTasks();
                    case 17 -> deleteTask();
                    case 18 -> sendMessage();
                    case 19 -> readMessage();
                    case 0 -> {
                        logger.info("Завершение работы программы.");
                        executor.shutdown();
                        return;
                    }
                    default -> System.out.println("Неизвестная команда. Попробуйте снова.");
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Ошибка при выполнении команды: " + e.getMessage(), e);
                System.out.println("Произошла ошибка. Пожалуйста, попробуйте снова.");
            }
        }
    }

    private static void sendMessage() {
        Long id = IdGenerators.createGenerator("message_id.txt").nextId();

        if (!isAuthenticated) {
            System.out.println("Вы не аутентифицированы");
        }
        findAllUsers();
        Long receiverId = (long) readIntInput("Выберите ID получателя");
        String content = readStringInput("Введите сообщение: ");

        messageService.sendMessage(id, currentUserId, receiverId, content);
        System.out.println("Сообщение отправлено");
    }

    private static void readMessage() {
        if (!isAuthenticated) {
            System.out.println("Вы не аутентифицированы");
            return;
        }
        List<Message> messages = messageService.getUserMessages(currentUserId);
        if (messages.isEmpty()) {
            System.out.println("Сообщений нет");
        } else {
            System.out.println("Ваши сообщения");
            for (Message message : messages) {
                String senderName = usersRepository.findById(message.getSenderId()).map(User::getName).orElse("Неизвестный");
                String receiverName = usersRepository.findById(message.getSenderId()).map(User::getName).orElse("Неизвестный");
                System.out.printf("[%s] %s -> %s: %s\n",
                        message.getTimestamp(),
                        senderName,
                        receiverName,
                        message.getContent());
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n=== Меню ===");
        System.out.println("1. Регистрация");
        System.out.println("2. Аутентификация");
        System.out.println("3. Получить список пользователей");
        System.out.println("4. Записать клиента");
        System.out.println("5. Получить список всех клиентов");
        System.out.println("6. Получить список клиентов менеджера");
        System.out.println("7. Удалить клиента");
        System.out.println("8. Поиск клиента");
        System.out.println("9. Изменить информацию о клиенте");
        System.out.println("10. Добавить контакт клиента");
        System.out.println("11. Вывести всю информацию о клиенте");
        System.out.println("12. Добавить сделку");
        System.out.println("13. Получить список всех сделок");
        System.out.println("14. Удалить сделку");
        System.out.println("15. Добавить задачу");
        System.out.println("16. Получить список всех задач");
        System.out.println("17. Удалить задачу");
        System.out.println("18. Отправить сообщение");
        System.out.println("19. Прочитать сообщения");
        System.out.println("0. Выход");
    }

    private static int readIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Ошибка: введите число.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static String readStringInput(String prompt) {
        System.out.print(prompt);
        scanner.nextLine(); // Очистка буфера
        return scanner.nextLine().trim();
    }

    private static void registerUser() {
        if (isAuthenticated) {
            System.out.println("Вы уже аутентифицированы.");
            return;
        }

        Long id = IdGenerators.createGenerator("users_id.txt").nextId();
        String email = readStringInput("Введите почту: ");
        String password = readStringInput("Введите пароль: ");
        String name = readStringInput("Введите имя: ");
        String lastName = readStringInput("Введите фамилию: ");
        String role = readStringInput("Введите должность (admin/manager/service/supervision): ");

        try {
            usersService.signUp(id, email, password, name, lastName, role(role));
            System.out.println("Пользователь успешно зарегистрирован.");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void authenticateUser() {
        String email = readStringInput("Введите почту: ");
        String password = readStringInput("Введите пароль: ");

        Optional<User> userOptional = usersRepository.findByEmail(email);
        if (userOptional.isPresent() && userOptional.get().getPassword().equals(password)) {
            isAuthenticated = true;
            currentUserId = userOptional.get().getId();
            System.out.println("Аутентификация прошла успешно.");
        } else {
            System.out.println("Неверные данные.");
        }
    }

    private static void findAllUsers() {
        if (!isAuthenticated) {
            System.out.println("Вы не аутентифицированы.");
            return;
        }

        List<UserDto> users = usersService.getUsers();
        if (users.isEmpty()) {
            System.out.println("Пользователи не найдены.");
        } else {
            System.out.println("Список пользователей:");
            for (UserDto user : users) {
                System.out.println(user.getId() + " " + user.getEmail());
            }
        }
    }

    private static void addClient() {
        if (!isAuthenticated) {
            System.out.println("Вы не аутентифицированы.");
            return;
        }

        long id = IdGenerators.createGenerator("client_id.txt").nextId();
        System.out.println("Сгенерирован id - " + id);

        findAllUsers();
        long idUser = readIntInput("Выберите id менеджера: ");
        String name = readStringInput("Введите имя: ");
        String email = readStringInput("Введите почту: ");
        String phone = readStringInput("Введите телефон: ");
        String address = readStringInput("Введите адрес: ");
        String status = readStringInput("Введите статус (1 - активный, 2 - удален): ");
        executor.execute(() -> {
            try {
                clientService.save(id, idUser, name, email, phone, address, status(status));
                System.out.println("Клиент успешно добавлен.");
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Ошибка при сохранении клиента: " + e.getMessage(), e);
            }
        });
    }

    private static void findAllClients() {
        List<ClientDto> clients = clientService.getClients();
        if (clients.isEmpty()) {
            System.out.println("Клиенты не найдены.");
        } else {
            System.out.println("Список клиентов:");
            for (ClientDto client : clients) {
                System.out.println(client.getId() + " " + client.getName() + " " + client.getEmail() + " " + client.getPhone() + " " + client.getAddress());
            }
        }
    }

    private static void findClientsByManager() {
        if (!isAuthenticated) {
            System.out.println("Вы не аутентифицированы.");
            return;
        }

        findAllUsers();
        long idUser = readIntInput("Выберите id менеджера: ");
        List<ClientDto> clients = clientService.getClientToUser(idUser);

        if (clients.isEmpty()) {
            System.out.println("Клиенты не найдены.");
        } else {
            System.out.println("Список клиентов менеджера:");
            for (ClientDto client : clients) {
                System.out.println(client.getId() + " " + client.getName() + " " + client.getEmail() + " " + client.getPhone() + " " + client.getAddress());
            }
        }
    }

    private static void deleteClient() {
        if (!isAuthenticated) {
            System.out.println("Вы не аутентифицированы.");
            return;
        }

        findAllClients();
        long idClient = readIntInput("Выберите id клиента: ");
        clientService.delete(clientService.getClientById(idClient), Client.Status.DELETE);
        System.out.println("Клиент успешно удален.");
    }

    private static void searchClient() {
        String search = readStringInput("Введите критерий поиска (имя, email или телефон): ");
        List<Client> clients = clientService.getClientBySearch(search);

        if (clients.isEmpty()) {
            System.out.println("Клиенты не найдены.");
        } else {
            System.out.println("Результаты поиска:");
            for (Client client : clients) {
                System.out.println(client);
            }
        }
    }

    private static void updateClientInfo() {
        if (!isAuthenticated) {
            System.out.println("Вы не аутентифицированы.");
            return;
        }

        findAllClients();
        long idClient = readIntInput("Выберите id клиента: ");
        int whatToChange = readIntInput("Выберите что изменить (1 - менеджер, 2 - имя, 3 - почта, 4 - телефон, 5 - адрес): ");
        String newInfo = readStringInput("Введите новое значение: ");
        executor.execute(() -> {
            try {
                clientService.updateClient(idClient, whatToChange, newInfo);
                System.out.println("Информация о клиенте успешно обновлена.");
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Ошибка при обновлении информации о клиенте: " + e.getMessage(), e);
                System.out.println("Ошибка при обновлении информации о клиенте.");
            }
        });
    }

    private static void addContact() {
        if (!isAuthenticated) {
            System.out.println("Вы не аутентифицированы.");
            return;
        }

        Long idContact = IdGenerators.createGenerator("contact_id.txt").nextId();
        System.out.println("Сгенерирован id - " + idContact);

        findAllClients();
        long idClient = readIntInput("Выберите id клиента: ");
        String name = readStringInput("Введите имя: ");
        String email = readStringInput("Введите почту: ");
        String phone = readStringInput("Введите телефон: ");
        String position = readStringInput("Введите должность: ");
        executor.execute(() -> {
            try {
                contactService.save(idContact, idClient, email, phone, name, position);
                System.out.println("Контакт успешно добавлен.");
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Ошибка при добавлении контакта: " + e.getMessage(), e);
                System.out.println("Ошибка при добавлении контакта.");
            }
        });
    }

    private static void showClientDetails() {
        findAllClients();
        long idClient = readIntInput("Выберите id клиента: ");
        List<Contact> contacts = contactService.contactGetClient(idClient);
        Client client = clientService.getClientById(idClient);

        System.out.println("Информация о клиенте:");
        System.out.println(client.getId() + " " + client.getName() + " " + client.getEmail() + " " + client.getPhone() + " " + client.getAddress());

        if (contacts.isEmpty()) {
            System.out.println("Контакты не найдены.");
        } else {
            System.out.println("Контакты клиента:");
            for (Contact contact : contacts) {
                System.out.println(contact.getId() + " " + contact.getName() + " " + contact.getEmail() + " " + contact.getPhone() + " " + contact.getPosition());
            }
        }
    }

    private static void addDeal() {
        if (!isAuthenticated) {
            System.out.println("Вы не аутентифицированы.");
            return;
        }

        long id = IdGenerators.createGenerator("deal_id.txt").nextId();
        System.out.println("Сгенерирован id - " + id);

        findAllClients();
        long idClient = readIntInput("Выберите id клиента: ");
        findAllUsers();
        long idUser = readIntInput("Выберите id пользователя: ");
        String title = readStringInput("Введите название сделки: ");
        double amount = readDoubleInput("Введите сумму сделки: ");
        String status = readStringInput("Введите статус сделки (NEW, PROGRESS, COMPLETED, FAILED): ");
        LocalDate createdDate = LocalDate.now();
        LocalDate closedDate = null;

        dealService.save(id, title, idClient, idUser, amount, Deal.Status.valueOf(status), createdDate, closedDate);
        System.out.println("Сделка успешно добавлена.");
    }

    private static void findAllDeals() {
        List<Deal> deals = dealRepository.findAll();
        if (deals.isEmpty()) {
            System.out.println("Сделки не найдены.");
        } else {
            System.out.println("Список сделок:");
            for (Deal deal : deals) {
                System.out.println(deal.getId() + " " + deal.getTitle() + " " + deal.getAmount() + " " + deal.getStatus());
            }
        }
    }

    private static void deleteDeal() {
        if (!isAuthenticated) {
            System.out.println("Вы не аутентифицированы.");
            return;
        }

        findAllDeals();
        long idDeal = readIntInput("Выберите id сделки: ");
        executor.execute(() -> {
            try {
                dealRepository.deleteById(idDeal);
                System.out.println("Сделка успешно удалена.");
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Ошибка при удалении клиента: " + e.getMessage(), e);
                System.out.println("Ошибка при удалении клиента.");
            }
        });
    }

    private static void addTask() {
        if (!isAuthenticated) {
            System.out.println("Вы не аутентифицированы.");
            return;
        }

        long id = IdGenerators.createGenerator("task_id.txt").nextId();
        System.out.println("Сгенерирован id - " + id);

        findAllClients();
        long idClient = readIntInput("Выберите id клиента: ");
        String title = readStringInput("Введите название задачи: ");
        String description = readStringInput("Введите описание задачи: ");
        findAllUsers();
        long assignedTo = readIntInput("Выберите id пользователя, которому назначена задача: ");
        String dueDate = readStringInput("Введите срок выполнения задачи (yyyy-MM-dd HH:mm:ss): ");
        String status = readStringInput("Введите статус задачи (CALL, MEETING, SALE): ");

        try {
            taskService.save(id, idClient, title, description, assignedTo, dueDate, Task.Status.valueOf(status));
            System.out.println("Задача успешно добавлена.");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void findAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        if (tasks.isEmpty()) {
            System.out.println("Задачи не найдены.");
        } else {
            System.out.println("Список задач:");
            for (Task task : tasks) {
                System.out.println(task.getId() + " " + task.getTitle() + " " + task.getDescription() + " " + task.getDueDate() + " " + task.getStatus());
            }
        }
    }

    private static void deleteTask() {
        if (!isAuthenticated) {
            System.out.println("Вы не аутентифицированы.");
            return;
        }

        findAllTasks();
        long idTask = readIntInput("Выберите id задачи: ");
        taskRepository.deleteById(idTask);
        System.out.println("Задача успешно удалена.");
    }

    private static double readDoubleInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.println("Ошибка: введите число.");
            scanner.next();
        }
        return scanner.nextDouble();
    }

    private static User.Role role(String role) {
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Роль не может быть пустой.");
        }

        return switch (role.trim().toLowerCase()) {
            case "admin" -> User.Role.ADMIN;
            case "manager" -> User.Role.MANAGER;
            case "service" -> User.Role.SERVICE;
            case "supervision" -> User.Role.SUPERVISION;
            default -> throw new IllegalArgumentException("Неверная роль: " + role);
        };
    }

    private static Client.Status status(String status) {
        return switch (status) {
            case "1" -> Client.Status.ACTIVE;
            case "2" -> Client.Status.DELETE;
            default -> throw new IllegalArgumentException("Неверный статус: " + status);
        };
    }
}