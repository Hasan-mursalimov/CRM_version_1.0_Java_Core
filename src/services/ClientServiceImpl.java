package services;


import dto.ClientDto;
import models.Client;
import repositories.ClientRepository;
import template.DocumentGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static dto.ClientDto.from;

/**
 * Сервис для управления клиентами.
 * Предоставляет методы для сохранения, поиска, обновления и удаления клиентов.
 */
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final DocumentGenerator documentGenerator;
    private static final ExecutorService executor = Executors.newFixedThreadPool(4);


    /**
     * Конструктор сервиса.
     *
     * @param clientRepository репозиторий для работы с клиентами
     */
    public ClientServiceImpl(ClientRepository clientRepository, DocumentGenerator documentGenerator) {
        this.clientRepository = clientRepository;
        this.documentGenerator = documentGenerator;
    }

    @Override
    public void save(Long id, Long userId, String name, String email, String phone, String address, Client.Status status) {
        if (id == null || userId == null || name == null || email == null || phone == null || address == null || status == null) {
            throw new IllegalArgumentException("Ни один из параметров не может быть null");
        }
        Client client = new Client(id, userId, name, email, phone, address, status);
        clientRepository.save(client);
        executor.execute(()->{
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("name", client.getName());
            placeholders.put("email", client.getEmail());
            placeholders.put("phone", client.getPhone());
            placeholders.put("address", client.getAddress());

            String documentContent = documentGenerator.generateDocument(placeholders);
            documentGenerator.saveDocument(documentContent, "sales_contract_" + client.getId() + ".txt");
        });
    }

    public void shutdown(){
        executor.shutdown();
    }
    @Override
    public List<ClientDto> getClients() {
        List<Client> clients = clientRepository.findAll();
        return from(clients);
    }


    @Override
    public List<ClientDto> getClientToUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("Идентификатор пользователя не может быть null");
        }
        List<Client> clients = clientRepository.findAllClientToUser(userId);
        return from(clients);
    }

    @Override
    public void delete(Client client, Client.Status status) {
        if (client == null || status == null) {
            throw new IllegalArgumentException("Клиент и статус не могут быть null");
        }
        clientRepository.updateClientStatus(client, status);
    }

    @Override
    public Client getClientById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Идентификатор клиента не может быть null");
        }
        return clientRepository.searchClientById(id);
    }

    @Override
    public List<Client> getClientBySearch(String search) {
        if (search == null || search.trim().isEmpty()) {
            throw new IllegalArgumentException("Критерий поиска не может быть null или пустым");
        }
        return clientRepository.searchClient(search);
    }

    @Override
    public void updateClient(Long id, int whatToChange, String newInfo) {
        if (id == null || newInfo == null) {
            throw new IllegalArgumentException("Идентификатор клиента и новое значение не могут быть null");
        }
        clientRepository.updateClientInfo(id, whatToChange, newInfo);
    }
}
