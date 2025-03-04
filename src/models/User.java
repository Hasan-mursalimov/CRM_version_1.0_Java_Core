package models;

import java.util.Objects;

/**
 * Класс, представляющий пользователя системы.
 * Содержит информацию о пользователе, такую как имя, email, пароль, роль и активность.
 */
public class User {
    private Long id;
    private String name;
    private String lastName;
    private String password;
    private String email;
    private Role role;
    private boolean isActive;
    private Status status;

    public enum Status{
        FIRED, WORKS
    }

    /**
     * Роли пользователя в системе.
     */
    public enum Role {
        ADMIN, MANAGER, SERVICE, SUPERVISION
    }

    /**
     * Конструктор для создания объекта пользователя.
     *
     * @param id        уникальный идентификатор пользователя
     * @param email     email пользователя
     * @param password  пароль пользователя
     * @param name      имя пользователя
     * @param lastName  фамилия пользователя
     * @param role      роль пользователя в системе
     */
    public User(Long id, String email, String password, String name, String lastName, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.lastName = lastName;
        this.name = name;
        this.role = role;
        this.status = Status.WORKS;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return isActive == user.isActive && Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(lastName, user.lastName) && Objects.equals(password, user.password) && Objects.equals(email, user.email) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lastName, password, email, role, isActive);
    }
}
