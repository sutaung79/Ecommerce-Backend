package com.ecom.app.controllers;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ecom.app.api.response.UserResponse;
import com.ecom.app.dto.UserDTO;
import com.ecom.app.services.UserService;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUsers() {
        
        UserResponse userResponse = new UserResponse();
        when(userService.getAllUsers(anyInt(), anyInt(), anyString(), anyString())).thenReturn(userResponse);

        
        ResponseEntity<UserResponse> response = userController.getUsers(1, 10, "name", "asc");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getBody()).isEqualTo(userResponse);
        verify(userService).getAllUsers(1, 10, "name", "asc");
    }
    
    @Test
    void testGetUser() {
        UserDTO userDTO = new UserDTO();
        when(userService.getUserById(1L)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.getUser(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getBody()).isEqualTo(userDTO);
        verify(userService).getUserById(1L);
    }
    
    @Test
    void testUpdateUser() {
        UserDTO userDTO = new UserDTO();
        when(userService.updateUser(1L, userDTO)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.updateUser(userDTO, 1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(userDTO);
        verify(userService).updateUser(1L, userDTO);
    }
    
    
    @Test
    void testDeleteUser() {
        String status = "User deleted";
        when(userService.deleteUser(1L)).thenReturn(status);

        ResponseEntity<String> response = userController.deleteUser(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(status);
        verify(userService).deleteUser(1L);
    }
}

