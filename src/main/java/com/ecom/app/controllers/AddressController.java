package com.ecom.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.app.dto.AddressDTO;
import com.ecom.app.entities.Address;
import com.ecom.app.services.AddressService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
@SecurityRequirement(name = "E-Commerce Application")
@Tag(name = "Address Management", description = "APIs for managing customer addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    
    @PostMapping("/address")
    @Operation(summary = "Create a new address", description = "Adds a new address to the database.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Address successfully created"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO) {
        AddressDTO savedAddressDTO = addressService.createAddress(addressDTO);
        return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
    }
    
    

    @GetMapping("/addresses")
    @Operation(summary = "Get all addresses", description = "Retrieves a list of all addresses.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "302", description = "Addresses found"),
        @ApiResponse(responseCode = "404", description = "No addresses found")
    })
    public ResponseEntity<List<AddressDTO>> getAddresses() {
        List<AddressDTO> addressDTOs = addressService.getAddresses();
        return new ResponseEntity<>(addressDTOs, HttpStatus.FOUND);
    }

    
    
    @GetMapping("/addresses/{addressId}")
    @Operation(summary = "Get an address by ID", description = "Retrieves details of a specific address by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "302", description = "Address found"),
        @ApiResponse(responseCode = "404", description = "Address not found")
    })
    public ResponseEntity<AddressDTO> getAddress(@PathVariable Long addressId) {
        AddressDTO addressDTO = addressService.getAddress(addressId);
        return new ResponseEntity<>(addressDTO, HttpStatus.FOUND);
    }
    
    

    @PutMapping("/addresses/{addressId}")
    @Operation(summary = "Update an address", description = "Updates an existing address with new information.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Address successfully updated"),
        @ApiResponse(responseCode = "404", description = "Address not found")
    })
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long addressId, @RequestBody Address address) {
        AddressDTO addressDTO = addressService.updateAddress(addressId, address);
        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }

    @DeleteMapping("/addresses/{addressId}")
    @Operation(summary = "Delete an address", description = "Removes an address from the system by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Address successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Address not found")
    })
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId) {
        String status = addressService.deleteAddress(addressId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
