package util;

import java.io.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

public class IdGeneratorFileBased implements IdGenerator {
    private final ReentrantLock lock = new ReentrantLock();

    private final String fileName;
    private long lastId = 0L;

    private synchronized void updateLastId(long id) {
        if (id > lastId) {
            lastId = id;
        }
    }

    private final Function<String, Long> lineToIdFunction = line -> {
        String[] parts = line.split("\\|");
        return (Long) Long.parseLong(parts[0]);
    };


    public IdGeneratorFileBased(String fileName) {
        this.fileName = fileName;
        try (Writer writer = new FileWriter(fileName, true)) {

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private Long findOldId() {
        if (lastId == 0L) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                lastId = reader.lines()
                        .map(lineToIdFunction)
                        .max(Long::compareTo)
                        .orElse(0L);
            } catch (IOException e) {
                throw new IllegalStateException("Ошибка чтения файла: " + e.getMessage(), e);
            }
        }
        return ++lastId;
    }

    @Override
    public Long nextId() {
        lock.lock();
        try {
            Long oldId = findOldId();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
                writer.write(oldId + "|" + "\n");
            } catch (IOException e) {
                throw new IllegalStateException("Ошибка при записи ID в файл: " + e.getMessage(), e);
            }
            return oldId;
        } finally {
            lock.unlock();
        }
    }
}


