package org.example.test.task.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.test.task.dto.UserDto;
import org.example.test.task.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody @Valid UserDto userDto) {
        return new ResponseEntity<>(userService.addUser(userDto), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long id, @RequestBody @Valid UserDto userDto) {
        userDto.setId(id);
        UserDto userDto1 = userService.updateUser(userDto);
        return new ResponseEntity<>(userDto1, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id){
        userService.deleteUserById(id);
        return new ResponseEntity<>("User was deleted from the list", HttpStatus.OK);
    }

    @GetMapping("/search")//("/search?from=2/20/2020&EndDate=2/21/2020")
    public ResponseEntity<List<UserDto>> getUserListFromTo(@RequestParam("from") String from, @RequestParam("to") String to) {
        return new ResponseEntity<>(userService.getUserByBirthDateFromTo(from, to), HttpStatus.OK);

    }
}
