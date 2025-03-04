package services;


import models.Task;
import repositories.TaskRepository;
import util.ValidationUtil;

public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void save(Long id, Long idClient, String title, String description, Long assignedTo,
                     String dueDate, Task.Status status) {
        ValidationUtil.checkNotNull(id, "Идентификатор id не может быть null");
        ValidationUtil.checkNotNull(idClient, "ID клиента не может быть null");
        ValidationUtil.checkNotEmpty(title, "Заголовок не может быть null");
        ValidationUtil.checkNotEmpty(description, "Описание не может быть null");
        ValidationUtil.checkNotNull(assignedTo, "Фамилия не может быть null");
        ValidationUtil.checkValidDateTimeString(dueDate, "Некорректный формат даты и времени. Используйте формат: yyyy-MM-dd HH:mm:ss");
        ValidationUtil.checkNotNull(status, "Статус не может быть null");
        Task task = new Task(id, idClient, title, description, assignedTo, dueDate, status);
        taskRepository.save(task);
    }

    @Override
    public void updateTask(Long id, String title, String description, Task.Status status) {
        if (id == null || title == null || description == null || status == null) {
            throw new IllegalArgumentException("Ни один из параметров не может быть null");
        }
        // Логика обновления задачи
    }

    @Override
    public void deleteTask(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Идентификатор задачи не может быть null");
        }
        // Логика удаления задачи
    }
}
