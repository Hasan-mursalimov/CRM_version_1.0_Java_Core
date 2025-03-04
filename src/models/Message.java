package models;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класс, представляющий сообщение.
 * Содержит информацию о сообщении, такую как отправитель, время отправки, текст сообщения и получатель.
 */
public class Message {
    private Long id;
    private Long senderId; // ID отправителя
    private Long receiverId; // ID получателя
    private String content; // Текст сообщения
    private LocalDateTime timestamp; // Время отправки

    public Message(Long id, Long senderId, Long receiverId, String content, LocalDateTime timestamp) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(id, message.id) && Objects.equals(senderId, message.senderId) && Objects.equals(receiverId, message.receiverId) && Objects.equals(content, message.content) && Objects.equals(timestamp, message.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, senderId, receiverId, content, timestamp);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
