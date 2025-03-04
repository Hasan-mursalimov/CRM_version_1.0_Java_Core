package repositories;


import models.Message;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message> {

    List<Message> findMessagesBySenderAndReceiver(Long senderId, Long receiverId);
    List<Message> findMessagesByUser(Long userId);
}
