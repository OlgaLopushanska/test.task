package org.example.test.task.mapper;

import org.example.test.task.dto.UserDto;
import org.example.test.task.entity.User;
import org.example.test.task.exception.InvalidBirthDateException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class UserMapper {
    private static final String DATE_PATTERN = "yyyy-MM-dd";

    public static UserDto mapToUserDto(User user) {

        UserDto userDto = new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(),
                String.valueOf(user.getBirthDate()), user.getAddress(), user.getPhoneNumber());
        return userDto;
    }

    public static User mapToUser(UserDto userDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        LocalDate localDate = convertToLocalDate(userDto.getBirthDate());
        User user = new User(userDto.getId(), userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(),
                localDate, userDto.getAddress(), userDto.getPhoneNumber());
        return user;
    }

    public static LocalDate convertToLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new InvalidBirthDateException("User", "birthDate", date);
        }
        return localDate;
    }
}
