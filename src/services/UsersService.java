package services;


import dto.UserDto;
import models.User;

import java.util.List;

public interface UsersService {
    void signUp(Long id, String login, String password, String name, String lastName, User.Role role);
    boolean signIn(String login, String password);
    List<UserDto> getUsers();
    void deleteUser(Long id);
}
