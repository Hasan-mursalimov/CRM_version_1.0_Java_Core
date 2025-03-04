package services;


import models.Contact;
import repositories.ContactRepository;

import java.util.List;
/**
 * Сервис для управления контактами.
 * Предоставляет методы для сохранения и поиска контактов.
 */
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    /**
     * Конструктор сервиса.
     *
     * @param contactRepository репозиторий для работы с контактами
     * @throws NullPointerException если contactRepository равен null
     */
    public ContactServiceImpl(ContactRepository contactRepository) {
        if (contactRepository == null) {
            throw new NullPointerException("ContactRepository не может быть null");
        }
        this.contactRepository = contactRepository;
    }

    /**
     * Сохраняет информацию о новом контакте.
     *
     * @param id       уникальный идентификатор контакта
     * @param clientId идентификатор клиента, к которому относится контакт
     * @param email    email контакта
     * @param phone    телефон контакта
     * @param name     имя контакта
     * @param position должность контакта
     * @throws IllegalArgumentException если любой из параметров некорректен
     */
    @Override
    public void save(Long id, Long clientId, String email, String phone, String name, String position) {
        if (id == null || clientId == null || email == null || phone == null || name == null || position == null) {
            throw new IllegalArgumentException("Ни один из параметров не может быть null");
        }
        if (email.trim().isEmpty() || phone.trim().isEmpty() || name.trim().isEmpty() || position.trim().isEmpty()) {
            throw new IllegalArgumentException("Поля email, phone, name и position не могут быть пустыми");
        }

        Contact contact = new Contact(id, clientId, name, email, phone, position);
        contactRepository.addingInformation(contact);
    }

    /**
     * Возвращает список всех контактов, связанных с указанным идентификатором клиента.
     *
     * @param clientId идентификатор клиента, для которого нужно найти контакты
     * @return список контактов, связанных с указанным клиентом
     * @throws IllegalArgumentException если clientId равен null
     */
    @Override
    public List<Contact> contactGetClient(Long clientId) {
        if (clientId == null) {
            throw new IllegalArgumentException("Идентификатор клиента не может быть null");
        }

        return contactRepository.findAllContactByClient(clientId);
    }

    @Override
    public void deleteContact(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Идентификатор контакта не может быть null");
        }
        // Логика удаления контакта
    }
}
