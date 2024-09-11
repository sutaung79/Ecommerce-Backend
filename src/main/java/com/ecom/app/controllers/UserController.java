package com.ecom.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecom.app.api.response.UserResponse;
import com.ecom.app.config.AppConstants;
import com.ecom.app.dto.UserDTO;
import com.ecom.app.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
@Tag(name = "User Operations", description = "Endpoints related to User management")
public class UserController {

    @Autowired
    private UserService userService;

    
    
    
    @GetMapping("/admin/users")
    @Operation(
        summary = "Get a list of users",
        description = "Retrieve a paginated and sorted list of all users in the system."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "302", description = "Users successfully found")
    })
    public ResponseEntity<UserResponse> getUsers(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_USERS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        UserResponse userResponse = userService.getAllUsers(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<UserResponse>(userResponse, HttpStatus.FOUND);
    }
    
    

    
    
    
    @GetMapping("/public/users/{userId}")
    @Operation(
        summary = "Get a specific user by ID",
        description = "Retrieve details of a specific user by their user ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "302", description = "User successfully found"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDTO> getUser(@PathVariable Long userId) {
        UserDTO user = userService.getUserById(userId);
        return new ResponseEntity<UserDTO>(user, HttpStatus.FOUND);
    }
    
    
    

    @PutMapping("/public/users/{userId}")
    @Operation(
        summary = "Update a user's information",
        description = "Update an existing user's details by providing the user ID and updated information."
    )
    @ApiResponse(responseCode = "200", description = "User successfully updated")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO, @PathVariable Long userId) {
        UserDTO updatedUser = userService.updateUser(userId, userDTO);
        return new ResponseEntity<UserDTO>(updatedUser, HttpStatus.OK);
    }

    
    
    @DeleteMapping("/admin/users/{userId}")
    @Operation(
        summary = "Delete a user by ID",
        description = "Delete a specific user from the system by providing their user ID."
    )
    @ApiResponse(responseCode = "200", description = "User successfully deleted")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        String status = userService.deleteUser(userId);
        return new ResponseEntity<String>(status, HttpStatus.OK);
    }
}
