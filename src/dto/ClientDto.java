package dto;


import models.Client;

import java.util.ArrayList;
import java.util.List;


public class ClientDto {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;

    public ClientDto(Long id, String name, String email, String phone, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public static ClientDto from (Client client){
        return new ClientDto(client.getId(), client.getName(), client.getEmail(),
                client.getPhone(), client.getAddress());
    }

    public static List<ClientDto> from(List<Client> clients){
        List<ClientDto> result = new ArrayList<>();
        for(Client client : clients){
            result.add(from(client));
        }
        return result;
    }

    @Override
    public String toString() {
        return "ClientDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
