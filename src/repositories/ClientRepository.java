package repositories;


import models.Client;

import java.util.List;

public interface ClientRepository extends CrudRepository<Client> {
    List<Client> findAllClientToUser(Long idUser);

    void updateClientStatus(Client updatedClient, Client.Status newStatus);

    Client searchClientById(Long id);

    List<Client> searchClient(String search);

    void updateClientInfo(Long updatedClientId, int whatToChange, String newMeaning);

}
