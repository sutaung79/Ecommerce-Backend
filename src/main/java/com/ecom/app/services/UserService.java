package com.ecom.app.services;

import com.ecom.app.api.response.UserResponse;
import com.ecom.app.dto.UserDTO;

public interface UserService {
	
	 UserDTO registerUser(UserDTO user);
	
	UserResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
	
	UserDTO getUserById(Long userId);
	
	UserDTO updateUser(Long userId, UserDTO userDTO);
	
	String deleteUser(Long userId);

	boolean userExistsByEmail(String email);
}
