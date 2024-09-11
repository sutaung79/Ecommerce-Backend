package com.ecom.app.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ecom.app.api.request.LoginCredentials;
import com.ecom.app.dto.UserDTO;
import com.ecom.app.exceptions.UserNotFoundException;
import com.ecom.app.security.JWTUtil;
import com.ecom.app.services.UserService;

import java.util.Collections;
import java.util.Map;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserService userService;

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterHandler() throws UserNotFoundException {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password");

        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode("password")).thenReturn(encodedPassword);
        when(userService.registerUser(any(UserDTO.class))).thenReturn(userDTO);
        when(jwtUtil.generateToken("test@example.com")).thenReturn("jwt-token");

        ResponseEntity<Map<String, Object>> response = authController.registerHandler(userDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().containsKey("jwt-token"));
        assertEquals("jwt-token", response.getBody().get("jwt-token"));

        verify(passwordEncoder).encode("password");
        verify(userService).registerUser(userDTO);
        verify(jwtUtil).generateToken("test@example.com");
    }


   
    @Test
    void testLoginHandler_Success() {
        LoginCredentials credentials = new LoginCredentials();
        credentials.setEmail("test@example.com");
        credentials.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtUtil.generateToken("test@example.com")).thenReturn("jwt-token");

        ResponseEntity<Map<String, Object>> response = authController.loginHandler(credentials);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.singletonMap("jwt-token", "jwt-token"), response.getBody());
    }

    @Test
    void testLoginHandler_InvalidCredentials() {
     
        LoginCredentials credentials = new LoginCredentials();
        credentials.setEmail("test@example.com");
        credentials.setPassword("wrongpassword");

        when(authenticationManager.authenticate(any())).thenThrow(BadCredentialsException.class);

        ResponseEntity<Map<String, Object>> response = authController.loginHandler(credentials);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(Collections.singletonMap("error", "Invalid username or password"), response.getBody());
    }

    
}

