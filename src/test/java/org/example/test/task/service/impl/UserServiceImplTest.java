package org.example.test.task.service.impl;

import org.example.test.task.dto.UserDto;
import org.example.test.task.entity.User;
import org.example.test.task.exception.EmailAlreadyExistsException;
import org.example.test.task.exception.InvalidBirthDateException;
import org.example.test.task.exception.InvalidRangeException;
import org.example.test.task.exception.UserNotExistsException;
import org.example.test.task.mapper.UserMapper;
import org.example.test.task.utils.UserUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


//@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;
    private List<User> userList = new ArrayList<>();
    private static final String dateFrom = "2000-01-01";
    private static final String dateTo = "2004-10-10";

    @Test
    public void addValidUser() {
        UserDto userDto = new UserDto(1L, "Taras", "Topolia", "topolia@gmail.com", "2003-01-11", "Peremogi 13", "123-12-12");
        UserDto userDtoFromService = userService.addUser(userDto);
        userList.add(UserMapper.mapToUser(userDto));
        assertTrue(userDto.equals(userDtoFromService));
    }

    @Test
    public void addUserWithEmailAlreadyExist_ThrowException() {
        UserDto userDto = new UserDto(2L, "Vlad", "Tichina", "tichina@gmail.com", "2000-01-11", "Shevchenka 13", "333-33-33");
        UserDto userDtoFromService = userService.addUser(userDto);
        userList.add(UserMapper.mapToUser(userDto));
        assertTrue(userDto.equals(userDtoFromService));
        EmailAlreadyExistsException exception = assertThrows(EmailAlreadyExistsException.class, () -> {
            userService.addUser(userDto);
        });
        String expectedMessage = "User with email " + userDto.getEmail() + " already exists";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    public void getAllUsersAfterAdding() {
        List<UserDto> userDtoList = userService.getAllUsers();
        assertEquals(userList.size(), userDtoList.size());
    }

    @Test
    public void updateExistingUser() {
        User userWithId1 = userService.getAllUsers().stream()
                .map(UserMapper::mapToUser)
                .filter(user -> user.getId().equals(1L))
                .findFirst()
                .orElseThrow(() -> new UserNotExistsException("User with id " + 1 + " not found"));
        userWithId1.setFirstName("Tanos");
        userWithId1.setLastName("Shevchuk");
        userWithId1.setEmail("shev@gmail.com");
        userWithId1.setBirthDate(LocalDate.parse("1991-09-19"));
        userWithId1.setAddress("Vokzalna 1");
        userWithId1.setPhoneNumber("099-90-90");
        UserDto userDto = userService.updateUser(UserMapper.mapToUserDto(userWithId1));
        assertEquals(userDto, UserMapper.mapToUserDto(userWithId1));
    }

    @Test
    public void updateUserWithInvalidId() {
        UserNotExistsException exception = assertThrows(UserNotExistsException.class, () -> {
            userService.updateUser(new UserDto(100L, "Oleh", "Polonskiy", "pol@gmail.com",
                    "2000-01-01", "Prorizna 7", "345-45-45"));
        });
        String expectedMessage = "User with id " + 100 + " not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void deleteUserById() {
        int sizeBefore = userService.getAllUsers().size();
        userService.deleteUserById(1L);
        int sizeAfter = userService.getAllUsers().size();
        assertEquals(sizeBefore - 1, sizeAfter);
    }

    @Test
    public void deleteUserById_WithinvalidId() {
        UserNotExistsException exception = assertThrows(UserNotExistsException.class, () -> {
            userService.deleteUserById(100L);
        });
        String expectedMessage = "User with id " + 100 + " not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void getUserByBirthDateFromTo() {
        userService.addUser(new UserDto(3L, "Pavalo", "Zelenin", "zel@gmail.com", "2002-01-01", "Uborevicha 11", "444-444-44"));
        int size = userService.getUserByBirthDateFromTo(dateFrom, dateTo).stream()
                .map(user->UserMapper.mapToUser(user))
                .filter(user -> !user.getBirthDate().isAfter(LocalDate.parse(dateFrom)) & user.getBirthDate().isBefore(LocalDate.parse(dateTo)))
                .collect(Collectors.toList())
                .size();
        assertEquals(size, 0);
    }
    @Test
    public void getUserByBirthDateFromTo_WithInvalidRange() {
        userService.addUser(new UserDto(3L, "Pavalo", "Zelenin", "zel@gmail.com", "2002-01-01", "Uborevicha 11", "444-444-44"));
        InvalidRangeException exception = assertThrows(InvalidRangeException.class, () -> {
            userService.getUserByBirthDateFromTo(dateTo, dateFrom);
        });
        String expectedMessage = "Date must be earlier than date to. Please enter correct dates";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}