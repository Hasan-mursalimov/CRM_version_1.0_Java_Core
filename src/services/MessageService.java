package services;


import models.Message;

import java.util.List;

public interface MessageService {
    void sendMessage(Long id, Long senderId, Long receiverId, String content);

    List<Message> getMessagesBetweenUsers(Long senderId, Long receiverId);

    List<Message> getUserMessages(Long userId);


}
