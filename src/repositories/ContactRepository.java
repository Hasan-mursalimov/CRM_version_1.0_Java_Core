package repositories;


import models.Contact;

import java.util.List;

public interface ContactRepository {
    void addingInformation(Contact model);

    void deleteInformation(Long contactClientId, int countDeleteContact);

    List<Contact> findAllContactByClient(Long contactClientId);
}
