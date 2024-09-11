package com.ecom.app.services;

import java.util.List;

import com.ecom.app.dto.AddressDTO;
import com.ecom.app.entities.Address;

public interface AddressService {
	
	AddressDTO createAddress(AddressDTO addressDTO);
	
	List<AddressDTO> getAddresses();
	
	AddressDTO getAddress(Long addressId);
	
	AddressDTO updateAddress(Long addressId, Address address);
	
	String deleteAddress(Long addressId);
	
}
