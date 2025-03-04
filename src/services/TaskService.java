package services;


import models.Task;

public interface TaskService {
    void save(Long id, Long idClient, String title, String description, Long assignedTo, String dueDate, Task.Status status);
    void updateTask(Long id, String title, String description, Task.Status status);
    void deleteTask(Long id);

}
