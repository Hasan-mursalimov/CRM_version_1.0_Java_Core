package models;

/**
 * Класс, представляющий контактное лицо клиента.
 * Содержит информацию о контактном лице, такую как имя, email, телефон и должность.
 */
public class Contact {
    private Long id;
    private Long clientId;
    private String name;
    private String email;
    private String phone;
    private String position;

    /**
     * Конструктор для создания объекта контактного лица.
     *
     * @param id       уникальный идентификатор контактного лица
     * @param clientId идентификатор клиента, к которому относится контактное лицо
     * @param name     имя контактного лица
     * @param email    email контактного лица
     * @param phone    телефон контактного лица
     * @param position должность контактного лица
     * @throws IllegalArgumentException если любой из параметров некорректен
     */
    public Contact(Long id, Long clientId, String name, String email, String phone, String position) {
        if (id == null || clientId == null || name == null || email == null || phone == null || position == null) {
            throw new IllegalArgumentException("Ни один из параметров не может быть null");
        }
        if (name.trim().isEmpty() || email.trim().isEmpty() || phone.trim().isEmpty() || position.trim().isEmpty()) {
            throw new IllegalArgumentException("Поля name, email, phone и position не могут быть пустыми");
        }

        this.id = id;
        this.clientId = clientId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.position = position;
    }

    /**
     * Конструктор для создания объекта контактного лица только с идентификатором.
     *
     * @param id уникальный идентификатор контактного лица
     */
    public Contact(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getClientId() {
        return clientId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPosition() {
        return position;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}

