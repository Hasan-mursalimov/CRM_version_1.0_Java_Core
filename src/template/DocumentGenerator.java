package template;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class DocumentGenerator {
    private final String templatePath;

    public DocumentGenerator(String templatePath) {
        this.templatePath = templatePath;
    }

    public String generateDocument(Map<String, String> placeholders) {
        try {
            // Чтение шаблона из файла
            String template = new String(Files.readAllBytes(Paths.get(templatePath)));

            // Замена плейсхолдеров на реальные данные
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                template = template.replace("{" + entry.getKey() + "}", entry.getValue());
            }

            return template;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении шаблона: " + e.getMessage(), e);
        }
    }

    public void saveDocument(String content, String outputPath) {
        try {
            // Сохранение документа в файл
            Files.write(Paths.get(outputPath), content.getBytes());
            System.out.println("Документ сохранен в " + outputPath);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении документа: " + e.getMessage(), e);
        }
    }
}
