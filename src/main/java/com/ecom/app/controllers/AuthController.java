package com.ecom.app.controllers;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.app.api.request.LoginCredentials;
import com.ecom.app.dto.UserDTO;
import com.ecom.app.security.JWTUtil;
import com.ecom.app.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
@Tag(name = "Authentication Operations", description = "Endpoints related to user authentication and registration")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    
    @PostMapping("/register")
    @Operation(
        summary = "Register a new user",
        description = "Registers a new user and generates a JWT token upon successful registration."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User successfully registered"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "User already exists")
    })
    public ResponseEntity<Map<String, Object>> registerHandler(@Valid @RequestBody UserDTO user) {
        // Check if user with the same email already exists
        if (userService.userExistsByEmail(user.getEmail())) {
        	
        	 return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                     .body(Collections.singletonMap("error", "User already Exist."));
        }

        String encodedPass = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPass);

        UserDTO userDTO = userService.registerUser(user);
        String token = jwtUtil.generateToken(userDTO.getEmail());

        return new ResponseEntity<>(Collections.singletonMap("jwt-token", token), HttpStatus.CREATED);
    }

    
    

    @PostMapping("/login")
    @Operation(
        summary = "User login",
        description = "Authenticates a user and generates a JWT token if the credentials are valid."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful, JWT token generated"),
        @ApiResponse(responseCode = "401", description = "Invalid username or password"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, Object>> loginHandler(@Valid @RequestBody LoginCredentials credentials) {
        try {
            UsernamePasswordAuthenticationToken authCredentials = new UsernamePasswordAuthenticationToken(
                    credentials.getEmail(), credentials.getPassword());

            //Authentication authentication = authenticationManager.authenticate(authCredentials);
            authenticationManager.authenticate(authCredentials);
            String token = jwtUtil.generateToken(credentials.getEmail());

            return ResponseEntity.ok(Collections.singletonMap("jwt-token", token));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Invalid username or password"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "An internal error occurred during authentication"));
        }
    }
}
