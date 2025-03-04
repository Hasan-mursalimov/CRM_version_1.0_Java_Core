package models;

import java.time.LocalDateTime;

public class Task {
    private Long id;

    private Long idClient;
    private String title;
    private String description;
    private Long assignedTo;
    private String dueDate;
    private LocalDateTime taskCreationDate;
    private Status status;

    public enum Status {
        CALL, MEETING, SALE
    }

    private Deal deal;

    public Task(Long id, Long idClient, String title, String description, Long assignedTo,String dueDate, Status status) {
        this.id = id;
        this.idClient = idClient;
        this.title = title;
        this.description = description;
        this.assignedTo = assignedTo;
        this.taskCreationDate = LocalDateTime.now();
        this.dueDate = dueDate;
        this.status = status;
    }

    public Task(Long id) {
        this.id = id;
    }

    public Long getIdClient() {
        return idClient;
    }

    public void setIdClient(Long idClient) {
        this.idClient = idClient;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Long assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getTaskCreationDate() {
        return taskCreationDate;
    }

    public void setTaskCreationDate(LocalDateTime taskCreationDate) {
        this.taskCreationDate = taskCreationDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
