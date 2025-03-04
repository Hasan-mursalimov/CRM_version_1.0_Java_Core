package dto;


import models.User;

import java.util.ArrayList;
import java.util.List;

public class UserDto {

    private Long id;
    private String email;

    public UserDto(Long id,String email) {
        this.id = id;
        this.email = email;
    }

    public static UserDto from (User user){
        return new UserDto(user.getId(),user.getEmail());
    }

    public static List<UserDto> from(List<User>users){
        List<UserDto> result = new ArrayList<>();
        for (User user : users){
            result.add(from(user));
        }
        return result;
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }
}
