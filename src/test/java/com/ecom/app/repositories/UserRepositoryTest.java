package com.ecom.app.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.ecom.app.entities.Address;
import com.ecom.app.entities.User;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;
    
    @BeforeEach
    public void setUp() {
        // Create an Address entity
        Address address1 = new Address();
        address1.setCity("Yangon");
        address1.setProvince("Yangon Region");
        address1.setStreet("123 Main St");
        address1.setBuilding("NO.501");
        address1.setPincode("11101");
        address1.setCountry("Myanmar");
        
        Address address2 = new Address();
        address2.setCity("Yangon");
        address2.setProvince("Yangon Region");
        address2.setStreet("130 Main St");
        address2.setBuilding("NO.50");
        address2.setPincode("11101");
        address2.setCountry("Myanmar");
        
        List<Address> addresses = List.of(address1, address2);

        // Create a User entity and associate it with the Address
        user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("test");
        user.setLastName("lastname");
        user.setPassword("password123");
        user.setMobileNumber("0979204509");
        user.setAddresses(addresses);
        
        userRepository.save(user);
    }
    
    @Test
    public void testFindByEmail() {
        Optional<User> foundUser = userRepository.findByEmail("test@example.com");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    public void testFindByEmail_NotFound() {
        Optional<User> foundUser = userRepository.findByEmail("notfound@example.com");
        assertThat(foundUser).isNotPresent();
    }

    @Test
    public void testFindByAddresses_AddressId() {
        // Retrieve the User with its associated addresses from the database
        User savedUser = userRepository.findByEmail("test@example.com").orElseThrow();
        
        // Assuming testing with the first address
        Long addressId = savedUser.getAddresses().get(0).getAddressId(); // Get the ID of the first saved address
        
        List<User> users = userRepository.findByAddresses_AddressId(addressId);
        assertThat(users).isNotEmpty();
        assertThat(users.get(0).getEmail()).isEqualTo("test@example.com");
    }

    @Test
    public void testFindByAddresses_AddressId_NotFound() {
        Long invalidAddressId = 999L; // Assuming this ID does not exist
        List<User> users = userRepository.findByAddresses_AddressId(invalidAddressId);
        assertThat(users).isEmpty();
    }
}
