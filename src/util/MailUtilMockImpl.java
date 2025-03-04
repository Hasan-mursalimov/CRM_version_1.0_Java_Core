package util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class MailUtilMockImpl implements MailUtil {

    private static final Logger logger = Logger.getLogger(MailUtilMockImpl.class.getName());
    private static final ExecutorService executor = Executors.newFixedThreadPool(4);

    @Override
    public void sendMail(String email, String text) {
        if (email == null || text == null) {
            throw new IllegalArgumentException("Email и текст сообщения не могут быть null");
        }
        executor.execute(()->{
            logger.info("Сообщение <" + text + "> было отправлено на почту: " + email);
        });

        // Имитация ошибки (для тестирования)
        if (email.contains("error")) {
            throw new RuntimeException("Ошибка при отправке сообщения на почту: " + email);
        }
    }
    public void shutdown(){
        executor.shutdown();
    }
}
