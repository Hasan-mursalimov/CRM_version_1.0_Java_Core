package models;

import java.util.Collections;
import java.util.List;

public class Client {

    private Long id;
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private Status status;
    private List<Task> tasks;
    private List<Contact> contacts;

    /**
     * Статусы клиента.
     */
    public enum Status {
        DELETE, ACTIVE
    }

    /**
     * Конструктор для создания объекта клиента.
     *
     * @param id       уникальный идентификатор клиента
     * @param userId   идентификатор пользователя, связанного с клиентом
     * @param name     имя клиента
     * @param email    email клиента
     * @param phone    телефон клиента
     * @param address  адрес клиента
     * @param status   статус клиента
     * @param tasks    список задач клиента
     * @param contacts список контактов клиента
     * @throws IllegalArgumentException если name, email, phone или address некорректны
     */
    public Client(Long id, Long userId, String name,
                  String email, String phone, String address, Status status,
                  List<Task> tasks, List<Contact> contacts) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя клиента не может быть null или пустым");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email клиента не может быть null или пустым");
        }
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Телефон клиента не может быть null или пустым");
        }
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Адрес клиента не может быть null или пустым");
        }
        if (status == null) {
            throw new IllegalArgumentException("Статус клиента не может быть null");
        }

        this.id = id;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.status = status;
        this.tasks = tasks != null ? Collections.unmodifiableList(tasks) : Collections.emptyList();
        this.contacts = contacts != null ? Collections.unmodifiableList(contacts) : Collections.emptyList();
    }


    /**
     * Конструктор для создания объекта клиента.
     *
     * @param id      уникальный идентификатор клиента
     * @param userId  идентификатор пользователя, связанного с клиентом
     * @param name    имя клиента
     * @param email   email клиента
     * @param phone   телефон клиента
     * @param address адрес клиента
     * @param status  статус клиента
     */
    public Client(Long id, Long userId, String name, String email, String phone, String address, Status status) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя клиента не может быть null или пустым");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email клиента не может быть null или пустым");
        }
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Телефон клиента не может быть null или пустым");
        }
        if (status == null) {
            throw new IllegalArgumentException("Статус клиента не может быть null");
        }
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) throws IllegalArgumentException {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", status=" + status +
                ", tasks=" + tasks +
                ", contacts=" + contacts +
                '}';
    }
}
