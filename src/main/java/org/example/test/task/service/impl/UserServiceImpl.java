package org.example.test.task.service.impl;

import lombok.AllArgsConstructor;
import org.example.test.task.dto.UserDto;
import org.example.test.task.entity.User;
import org.example.test.task.exception.EmailAlreadyExistsException;
import org.example.test.task.exception.InvalidRangeException;
import org.example.test.task.exception.UserNotExistsException;
import org.example.test.task.mapper.UserMapper;
import org.example.test.task.service.UserService;

import org.example.test.task.utils.UserUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private List<User> userList;

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto);
        user.setId(UserUtils.getId());
        if (!userList.contains(user)) {
            userList.add(user);
        } else {
            throw new EmailAlreadyExistsException("User with email " + user.getEmail() + " already exists");
        }
        User userFromList = userList.stream()
                .filter(e -> e.getEmail().equals(userDto.getEmail()))
                .findFirst()
                .get();
        return UserMapper.mapToUserDto(userFromList);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userList.stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getUserByBirthDateFromTo(String fromDate, String toDate) {
        LocalDate dateFrom = UserMapper.convertToLocalDate(fromDate);
        LocalDate dateTo = UserMapper.convertToLocalDate(toDate);
        if (dateFrom.isAfter(dateTo)) {
            throw new InvalidRangeException("Date must be earlier than date to. Please enter correct dates");
        }
        List<User> userListFromTo = userList.stream()
                .filter(user -> user.getBirthDate().isAfter(dateFrom) & user.getBirthDate().isBefore(dateTo))
                .collect(Collectors.toList());
        return userListFromTo.stream()
                .map(user -> UserMapper.mapToUserDto(user))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto);
        User userFromList = userList.stream()
                .filter(userr -> userr.getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new UserNotExistsException("User with id " + user.getId() + " not found"));
        int index = userList.indexOf(userFromList);
        userList.set(index, user);
        return UserMapper.mapToUserDto(userList.get(index));
    }

    @Override
    public void deleteUserById(Long id) {
        User userFromList = userList.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new UserNotExistsException("User with id " + id + " not found"));
        userList.remove(userFromList);
    }
}

