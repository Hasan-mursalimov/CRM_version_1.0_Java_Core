package template;

import dto.ClientDto;

public class Builder {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;

    public Builder setId(Long id) {
        this.id = id;
        return this;
    }

    public Builder setName(String name) {
        this.name = name;
        return this;
    }

    public Builder setEmail(String email) {
        this.email = email;
        return this;
    }

    public Builder setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Builder setAddress(String address) {
        this.address = address;
        return this;
    }

    public ClientDto build() {
        return new ClientDto(id, name, email, phone, address);
    }

}
