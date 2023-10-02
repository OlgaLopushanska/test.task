package org.example.test.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.test.task.dto.UserDto;
import org.example.test.task.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import org.springframework.http.MediaType;


import java.util.ArrayList;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    public void UserController_CreateUser() throws Exception{
        UserDto expected = new UserDto(1L, "Taras", "Petrenko", "petr@gmail.com",
                "2000-10-02","Peremogi 9", "898-89-89");
        when(userService.addUser(any(UserDto.class))).thenReturn(expected);
        MvcResult mvcResult= mockMvc.perform(post("/api/users").content("{\"firstName\": \"Taras\", " +
                        "\"lastName\": \"Petrenko\"," +
                        "\"email\": \"petr@gmail.com\", \"birthDate\": \"2000-10-02\", " +
                        "\"address\": \"Peremogi 9\", \"phoneNumber\":\"898-89-89\"}").contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isCreated()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        UserDto userDto = new ObjectMapper().readValue(content, UserDto.class);
        assertThat(userDto).isEqualTo(expected);
   }

    @Test
    public void getAllUsers() throws Exception {
        List<UserDto> listUserDto = new ArrayList<>();
        UserDto expected = new UserDto(1L, "Taras", "Petrenko", "petr@gmail.com",
                "2000-10-02","Peremogi 9", "898-89-89");
        listUserDto.add(expected);
        when(userService.getAllUsers()).thenReturn(listUserDto);
        MvcResult mvcResult = mockMvc.perform(get("/api/users")).andDo(print()).andExpect(status().isOk()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        List userDto = new ObjectMapper().readValue(contentAsString, List.class);
        assertThat(userDto.get(0).toString()).isEqualTo(expected.toString());
    }

    @Test
    void updateUser() throws Exception {
        UserDto expected = new UserDto(1L, "Petro", "Pravdiviy", "pravd@gmail.com",
                "2004-10-02","Tichini 19", "777-77-89");
        when(userService.updateUser(any(UserDto.class))).thenReturn(expected);
        MvcResult mvcResult= mockMvc.perform(put("/api/users/1").content("{\"firstName\": \"Petro\", " +
                        "\"lastName\": \"Pravdiviy\"," +
                        "\"email\": \"pravd@gmail.com\", \"birthDate\": \"2004-10-02\", " +
                        "\"address\": \"Tichini 19\", \"phoneNumber\":\"777-77-89\"}").contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        UserDto userDto = new ObjectMapper().readValue(content, UserDto.class);
        assertThat(userDto).isEqualTo(expected);
    }

    @Test
    public void deleteUser() throws Exception {
        String result = "User was deleted from the list";
        MvcResult mvcResult = mockMvc.perform(delete("/api/users/1")).andDo(print()).andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertThat(content).isEqualTo(result);
    }
}