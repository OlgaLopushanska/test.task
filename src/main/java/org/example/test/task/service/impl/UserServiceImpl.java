package org.example.test.task.service.impl;

import lombok.AllArgsConstructor;
import org.example.test.task.dto.UserDto;
import org.example.test.task.entity.User;
import org.example.test.task.exception.EmailAlreadyExistsException;
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
    private List<User> userDtoList;
    public ModelMapper modelMapper;

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto);
        user.setId(UserUtils.getId());
        if (!userDtoList.contains(user)) {
            userDtoList.add(user);
        } else {
            throw new EmailAlreadyExistsException("User with email " + user.getEmail() + " already exists");
        }
        User userFromList = userDtoList.stream()
                .filter(e -> e.getEmail().equals(userDto.getEmail()))
                .findFirst()
                .get();
        return UserMapper.mapToUserDto(userFromList);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userDtoList.stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getUserByBirthDateFromTo(String fromDate, String toDate) {
        LocalDate dateFrom = UserMapper.convertToLocalDate(fromDate);
        LocalDate dateTo = UserMapper.convertToLocalDate(toDate);
        List<User> userList = userDtoList.stream()
                .filter(user -> user.getBirthDate().isAfter(dateFrom) & user.getBirthDate().isBefore(dateTo))
                .collect(Collectors.toList());
        return userList.stream()
                .map(user -> UserMapper.mapToUserDto(user))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto);
        if (userDtoList.contains(user)) {
            int index = userDtoList.indexOf(user);
            userDtoList.set(index, user);
            return UserMapper.mapToUserDto(userDtoList.get(index));
        } else {
            throw new UserNotExistsException("User with email " + user.getEmail() + " does not exist");
        }
    }

    @Override
    public void deleteUserById(Long id) {
        User userFromList = userDtoList.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new UserNotExistsException("User with id " + id + " not found"));
        userDtoList.remove(userFromList);
    }
}

