package models;

import java.time.LocalDate;
/**
 * Класс, представляющий сделку.
 * Содержит информацию о сделке, такую как название, идентификаторы клиента и пользователя,
 * сумма, статус, дата создания и дата закрытия.
 */
public class Deal {
    private Long id;
    private String title;
    private Long clientId;
    private Long userId;
    private Double amount;
    private Status status;
    private LocalDate createdDate;
    private LocalDate closedDate;
    /**
     * Статусы сделки.
     */
    public enum Status {
        NEW, PROGRESS, COMPLETED, FAILED
    }

    /**
     * Конструктор для создания объекта сделки.
     *
     * @param id          уникальный идентификатор сделки
     * @param title       название сделки
     * @param clientId    идентификатор клиента
     * @param userId      идентификатор пользователя
     * @param amount      сумма сделки
     * @param status      статус сделки
     * @param createdDate дата создания сделки
     * @param closedDate  дата закрытия сделки
     * @throws IllegalArgumentException если любой из параметров некорректен
     */
    public Deal(Long id, String title, Long clientId, Long userId,
                Double amount, Status status, LocalDate createdDate, LocalDate closedDate) {
        if (id == null || title == null || clientId == null || userId == null || amount == null || status == null || createdDate == null) {
            throw new IllegalArgumentException("Ни один из параметров не может быть null");
        }
        if (title.trim().isEmpty() || amount < 0) {
            throw new IllegalArgumentException("Название сделки не может быть пустым, а сумма должна быть положительной");
        }
        this.id = id;
        this.title = title;
        this.clientId = clientId;
        this.userId = userId;
        this.amount = amount;
        this.status = status;
        this.createdDate = createdDate;
        this.closedDate = closedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(LocalDate closedDate) {
        this.closedDate = closedDate;
    }
}
