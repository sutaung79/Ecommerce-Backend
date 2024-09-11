package com.ecom.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecom.app.entities.User;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	
	Optional<User> findByEmail(String email);
	
	List<User> findByAddresses_AddressId(Long addressId);

    
    boolean existsByEmail(String email);
   
    
}

