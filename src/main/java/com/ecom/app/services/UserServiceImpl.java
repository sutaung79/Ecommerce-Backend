package com.ecom.app.services;


import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecom.app.api.response.UserResponse;
import com.ecom.app.config.AppConstants;
import com.ecom.app.dto.AddressDTO;
import com.ecom.app.dto.CartDTO;
import com.ecom.app.dto.ProductDTO;
import com.ecom.app.dto.UserDTO;
import com.ecom.app.entities.Address;
import com.ecom.app.entities.Cart;
import com.ecom.app.entities.CartItem;
import com.ecom.app.entities.Role;
import com.ecom.app.entities.User;
import com.ecom.app.exceptions.APIException;
import com.ecom.app.exceptions.ResourceNotFoundException;
import com.ecom.app.repositories.AddressRepository;
import com.ecom.app.repositories.RoleRepository;
import com.ecom.app.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private AddressRepository addressRepo;

	@Autowired
	private CartService cartService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ModelMapper modelMapper;

//	@Override
//	public UserDTO registerUser(UserDTO userDTO) {
//
//		try {
//			User user = modelMapper.map(userDTO, User.class);
//
//			Cart cart = new Cart();
//			user.setCart(cart);
//			
//			Role role = roleRepo.findById(AppConstants.USER_ID).get();
//			user.getRoles().add(role);
//
//			String country = userDTO.getAddress().getCountry();
//			String province = userDTO.getAddress().getProvince();
//			String city = userDTO.getAddress().getCity();
//			String pincode = userDTO.getAddress().getPincode();
//			String street = userDTO.getAddress().getStreet();
//			String building = userDTO.getAddress().getBuilding();
//
//			Address address = addressRepo.findByStreetAndBuildingAndCityAndProvinceAndCountryAndPincode(street, building,
//					city,province, building, pincode);
//
//			if (address == null) {
//				address = new Address(street, building, city, province,country,pincode);
//
//				address = addressRepo.save(address);
//			}
//
//			user.setAddresses(List.of(address));
//
//			User registeredUser = userRepo.save(user);
//
//			cart.setUser(registeredUser);
//
//			userDTO = modelMapper.map(registeredUser, UserDTO.class);
//
//			userDTO.setAddress(modelMapper.map(user.getAddresses().stream().findFirst().get(), AddressDTO.class));
//
//			return userDTO;
//		} catch (DataIntegrityViolationException e) {
//			throw new APIException("User already exists with emailId: " + userDTO.getEmail());
//		}
//
//	}
	
	public UserDTO registerUser(UserDTO userDTO) {
	    try {
	        User user = modelMapper.map(userDTO, User.class);

	        Cart cart = new Cart();
	        user.setCart(cart);

	        Role role = roleRepo.findById(AppConstants.USER_ID).orElseThrow(() -> new ResourceNotFoundException("Role", "roleId", AppConstants.USER_ID));
	        user.getRoles().add(role);

	        Address address = null;
	        if (userDTO.getAddress() != null) {
	            String country = userDTO.getAddress().getCountry();
	            String province = userDTO.getAddress().getProvince();
	            String city = userDTO.getAddress().getCity();
	            String pincode = userDTO.getAddress().getPincode();
	            String street = userDTO.getAddress().getStreet();
	            String building = userDTO.getAddress().getBuilding();

	            address = addressRepo.findByStreetAndBuildingAndCityAndProvinceAndCountryAndPincode(
	                    street, building, city, province, country, pincode);

	            if (address == null) {
	                address = new Address(street, building, city, province, country, pincode);
	                address = addressRepo.save(address);
	            }
	        }

	        if (address != null) {
	            user.setAddresses(List.of(address));
	        }

	        User registeredUser = userRepo.save(user);
	        cart.setUser(registeredUser);

	        UserDTO registeredUserDTO = modelMapper.map(registeredUser, UserDTO.class);

	        if (registeredUser.getAddresses() != null && !registeredUser.getAddresses().isEmpty()) {
	            registeredUserDTO.setAddress(modelMapper.map(registeredUser.getAddresses().get(0), AddressDTO.class));
	        }

	        return registeredUserDTO;
	    } catch (DataIntegrityViolationException e) {
	        throw new APIException("User already exists with emailId: " + userDTO.getEmail());
	    }
	}

	

	@Override
	public UserResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		
		Page<User> pageUsers = userRepo.findAll(pageDetails);
		
		List<User> users = pageUsers.getContent();

		if (users.size() == 0) {
			throw new APIException("No User exists !!!");
		}

		List<UserDTO> userDTOs = users.stream().map(user -> {
			UserDTO dto = modelMapper.map(user, UserDTO.class);

			if (user.getAddresses().size() != 0) {
				dto.setAddress(modelMapper.map(user.getAddresses().stream().findFirst().get(), AddressDTO.class));
			}

			CartDTO cart = modelMapper.map(user.getCart(), CartDTO.class);

			List<ProductDTO> products = user.getCart().getCartItems().stream()
					.map(item -> modelMapper.map(item.getProduct(), ProductDTO.class)).collect(Collectors.toList());

			dto.setCart(cart);

			dto.getCart().setProducts(products);

			return dto;

		}).collect(Collectors.toList());

		UserResponse userResponse = new UserResponse();
		
		userResponse.setContent(userDTOs);
		userResponse.setPageNumber(pageUsers.getNumber());
		userResponse.setPageSize(pageUsers.getSize());
		userResponse.setTotalElements(pageUsers.getTotalElements());
		userResponse.setTotalPages(pageUsers.getTotalPages());
		userResponse.setLastPage(pageUsers.isLast());
		
		return userResponse;
	}

	@Override
	public UserDTO getUserById(Long userId) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

		UserDTO userDTO = modelMapper.map(user, UserDTO.class);

		userDTO.setAddress(modelMapper.map(user.getAddresses().stream().findFirst().get(), AddressDTO.class));

		CartDTO cart = modelMapper.map(user.getCart(), CartDTO.class);

		List<ProductDTO> products = user.getCart().getCartItems().stream()
				.map(item -> modelMapper.map(item.getProduct(), ProductDTO.class)).collect(Collectors.toList());

		userDTO.setCart(cart);

		userDTO.getCart().setProducts(products);

		return userDTO;
	}

	@Override
	public UserDTO updateUser(Long userId, UserDTO userDTO) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

		String encodedPass = passwordEncoder.encode(userDTO.getPassword());

		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		user.setMobileNumber(userDTO.getMobileNumber());
		user.setEmail(userDTO.getEmail());
		user.setPassword(encodedPass);

		if (userDTO.getAddress() != null) {
			
			String country = userDTO.getAddress().getCountry();
			String province = userDTO.getAddress().getProvince();
			String city = userDTO.getAddress().getCity();
			String pincode = userDTO.getAddress().getPincode();
			String street = userDTO.getAddress().getStreet();
			String building = userDTO.getAddress().getBuilding();

			Address address = addressRepo.findByStreetAndBuildingAndCityAndProvinceAndCountryAndPincode(street, building,
					city, province, country, pincode);

			if (address == null) {
				address = new Address(street, building, city, province, country , pincode);

				address = addressRepo.save(address);

				user.setAddresses(List.of(address));
			}
		}

		userDTO = modelMapper.map(user, UserDTO.class);

		userDTO.setAddress(modelMapper.map(user.getAddresses().stream().findFirst().get(), AddressDTO.class));

		CartDTO cart = modelMapper.map(user.getCart(), CartDTO.class);

		List<ProductDTO> products = user.getCart().getCartItems().stream()
				.map(item -> modelMapper.map(item.getProduct(), ProductDTO.class)).collect(Collectors.toList());

		userDTO.setCart(cart);

		userDTO.getCart().setProducts(products);

		return userDTO;
	}

	@Override
	public String deleteUser(Long userId) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

		List<CartItem> cartItems = user.getCart().getCartItems();
		Long cartId = user.getCart().getCartId();

		cartItems.forEach(item -> {

			Long productId = item.getProduct().getProductId();

			cartService.deleteProductFromCart(cartId, productId);
		});

		userRepo.delete(user);

		return "User with userId " + userId + " deleted successfully!!!";
	}
	
	@Override
    public boolean userExistsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }
	
	
	

}


