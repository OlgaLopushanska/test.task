package org.example.test.task.service;

import org.example.test.task.dto.UserDto;
import java.util.List;



public interface UserService {
    UserDto addUser(UserDto userDto);
    List<UserDto> getAllUsers();
    List<UserDto> getUserByBirthDateFromTo(String fromDate, String toDate);
    UserDto updateUser(UserDto userDto);
    void deleteUserById(Long id);
}
