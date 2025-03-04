package services;


import models.Contact;

import java.util.List;

public interface ContactService {
    void save(Long id, Long clientId,String email, String phone, String name, String position);

    List<Contact> contactGetClient(Long clientId);

    void deleteContact(Long id);
}
