package repositories;


import models.Task;
import util.IdGenerator;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Реализация репозитория для работы с задачами, основанная на файловой системе.
 * Предоставляет методы для сохранения, поиска и изменения задач.
 */
public class TaskRepositoryFileBasedImpl implements TaskRepository {

    // Создаем логгер
    private static final Logger logger = Logger.getLogger(TaskRepositoryFileBasedImpl.class.getName());

    private final String fileName;
    private final IdGenerator idGenerator;
    private final Map<Long, Task> taskCache;

    public TaskRepositoryFileBasedImpl(String fileName, IdGenerator idGenerator) {
        this.fileName = fileName;
        this.idGenerator = idGenerator;
        this.taskCache = loadTasks();
    }

    private Map<Long, Task> loadTasks() {
        Map<Long, Task> cache = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    Task task = lineToTaskFunction.apply(line);
                    cache.put(task.getId(), task);
                } catch (IllegalArgumentException e) {
                    logger.log(Level.SEVERE, "Ошибка в строке: " + line + " - " + e.getMessage(), e);
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Ошибка чтения файла: " + e.getMessage(), e);
            throw new IllegalStateException("Ошибка чтения файла", e);
        }
        return cache;
    }

    private final Function<String, Task> lineToTaskFunction = line -> {
        String[] parts = line.split("\\|");
        if (parts.length < 8) {
            throw new IllegalArgumentException("Некорректный формат строки: " + line);
        }
        String id = parts[0];
        String idClient = parts[1];
        String title = parts[2];
        String description = parts[3];
        String assignedTo = parts[4];
        String dueDate = parts[6];
        String status = parts[7];
        return new Task(Long.parseLong(id), Long.parseLong(idClient), title,
                description, Long.parseLong(assignedTo), dueDate, Task.Status.valueOf(status.toUpperCase()));
    };

    @Override
    public void save(Task model) {
        if (model == null) {
            throw new IllegalArgumentException("Модель задачи не может быть null");
        }
        if (model.getTitle() == null || model.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Название задачи не может быть пустым");
        }

        model.setId(idGenerator.nextId());
        taskCache.put(model.getId(), model);
        saveTasksToFile();
    }

    private void saveTasksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Task task : taskCache.values()) {
                writer.write(String.format("%d|%d|%s|%s|%s|%s|%s\n",
                        task.getId(),
                        task.getIdClient(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getAssignedTo(),
                        task.getTaskCreationDate(),
                        task.getDueDate(),
                        task.getStatus(), "\n"));
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Ошибка при сохранении задач в файл: " + e.getMessage(), e);
            throw new RuntimeException("Ошибка при сохранении задач в файл: " + e.getMessage(), e);
        }
    }

    @Override
    public void taskChange(Long idTask, int whatToChange, String newMeaning) {
        Task task = taskCache.get(idTask);
        if (task == null) {
            throw new RuntimeException("Задача с таким ID не найдена");
        }
        switch (whatToChange) {
            case 1 -> task.setTitle(newMeaning);
            case 2 -> task.setDescription(newMeaning);
            case 3 -> task.setAssignedTo(Long.parseLong(newMeaning));
            case 4 -> task.setDueDate(newMeaning);
            case 5 -> task.setStatus(Task.Status.valueOf(newMeaning.toUpperCase()));
            default -> throw new IllegalArgumentException("Некорректный индекс поля для изменения");
        }
        saveTasksToFile();
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(taskCache.values());
    }

    @Override
    public void deleteById(Long id) {
        taskCache.remove(id);
        saveTasksToFile();
    }
}
