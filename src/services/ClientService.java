package services;


import dto.ClientDto;
import models.Client;

import java.util.List;

public interface ClientService {
    void save(Long id, Long userId, String name, String email, String phone, String address, Client.Status status);

    List<ClientDto> getClients();


    List<ClientDto> getClientToUser(Long userId);

    void delete(Client client, Client.Status status);

    Client getClientById(Long id);

    List<Client> getClientBySearch(String search);

    void updateClient(Long id, int whatToChange, String newInfo);
}
