package repositories;



import models.Deal;
import util.IdGenerator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Реализация репозитория для работы со сделками, основанная на файловой системе.
 * Предоставляет методы для сохранения сделок и их чтения из файла.
 */
public class DealRepositoryFileBasedImpl implements DealRepository {
    /**
     * Имя файла, в котором хранятся данные о сделках.
     */
    private final String fileName;

    /**
     * Генератор уникальных идентификаторов для новых сделок.
     */
    private final IdGenerator idGenerator;

    /**
     * Форматтер для преобразования дат в строку и обратно.
     */
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Конструктор для создания экземпляра репозитория.
     *
     * @param fileName    имя файла для хранения данных
     * @param idGenerator генератор уникальных идентификаторов
     * @throws NullPointerException если fileName или idGenerator равен null
     */
    public DealRepositoryFileBasedImpl(String fileName, IdGenerator idGenerator) {
        if (fileName == null || idGenerator == null) {
            throw new NullPointerException("Параметры fileName и idGenerator не могут быть null");
        }
        this.fileName = fileName;
        this.idGenerator = idGenerator;
    }

    /**
     * Функция для преобразования строки из файла в объект {@link Deal}.
     * Формат строки: id|title|clientId|userId|amount|status|createdDate|closedDate
     *
     * @param line строка из файла
     * @return объект {@link Deal}
     * @throws IllegalArgumentException если строка имеет некорректный формат
     */
    private final Function<String, Deal> lineToDealFunction = line -> {
        String[] parts = line.split("\\|");

        // Проверка на корректное количество частей
        if (parts.length < 8) {
            throw new IllegalArgumentException("Некорректный формат строки: " + line);
        }

        // Парсинг данных
        Long id = Long.parseLong(parts[0]);
        String title = parts[1];
        Long clientId = Long.parseLong(parts[2]);
        Long userId = Long.parseLong(parts[3]);
        Double amount = Double.parseDouble(parts[4]);
        Deal.Status status = Deal.Status.valueOf(parts[5]);
        LocalDate createdDate = LocalDate.parse(parts[6], dateFormatter);
        LocalDate closedDate = parts[7].isEmpty() ? null : LocalDate.parse(parts[7], dateFormatter);

        return new Deal(id, title, clientId, userId, amount, status, createdDate, closedDate);
    };

    /**
     * Сохраняет сделку в файл.
     *
     * @param model объект {@link Deal}, который необходимо сохранить
     * @throws RuntimeException если произошла ошибка при записи данных в файл
     */
    @Override
    public void save(Deal model) {
        if (model == null) {
            throw new IllegalArgumentException("Модель сделки не может быть null");
        }

        model.setId(idGenerator.nextId());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            String createdDateStr = model.getCreatedDate().format(dateFormatter);
            String closedDateStr = model.getClosedDate() == null ? "" : model.getClosedDate().format(dateFormatter);

            writer.write(String.format("%d|%s|%d|%d|%.2f|%s|%s|%s\n",
                    model.getId(),
                    model.getTitle(),
                    model.getClientId(),
                    model.getUserId(),
                    model.getAmount(),
                    model.getStatus(),
                    createdDateStr,
                    closedDateStr));
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении сделки в файл: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Deal> findAll() {
        List<Deal> deals = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    Deal deal = lineToDealFunction.apply(line);
                    deals.add(deal);
                } catch (IllegalArgumentException e) {
                    System.err.println("Ошибка при чтении строки: " + line + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Ошибка при чтении файла: " + e.getMessage(), e);
        }
        return deals;
    }

    @Override
    public void deleteById(Long id) {
        try {
            Path tempFile = Files.createTempFile("temp", ".txt");

            try (BufferedReader reader = Files.newBufferedReader(Path.of(fileName));
                 BufferedWriter writer = Files.newBufferedWriter(tempFile)) {
                String line;
                while ((line = reader.readLine()) != null){
                    if (!line.startsWith(id + "|")){
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }
            Files.move(tempFile, Path.of(fileName), StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            throw new RuntimeException("Ошибка при удалении контакта: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateDeal(Deal updatedDeal) {

    }
}
