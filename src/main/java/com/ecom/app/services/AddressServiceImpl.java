package com.ecom.app.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom.app.dto.AddressDTO;
import com.ecom.app.entities.Address;
import com.ecom.app.entities.User;
import com.ecom.app.exceptions.APIException;
import com.ecom.app.exceptions.ResourceNotFoundException;
import com.ecom.app.repositories.AddressRepository;
import com.ecom.app.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	private AddressRepository addressRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public AddressDTO createAddress(AddressDTO addressDTO) {

		String street = addressDTO.getStreet();
		String building = addressDTO.getBuilding();
		String city = addressDTO.getCity();
		String province = addressDTO.getProvince();
		String country = addressDTO.getCountry();
		String pincode = addressDTO.getPincode();

		Address addressFromDB = addressRepo.findByStreetAndBuildingAndCityAndProvinceAndCountryAndPincode(street,
				 building,city, province,country, pincode);

		if (addressFromDB != null) {
			throw new APIException("Address already exists with addressId: " + addressFromDB.getAddressId());
		}

		Address address = modelMapper.map(addressDTO, Address.class);

		Address savedAddress = addressRepo.save(address);

		return modelMapper.map(savedAddress, AddressDTO.class);
	}

	
	@Override
	public List<AddressDTO> getAddresses() {
		List<Address> addresses = addressRepo.findAll();

		List<AddressDTO> addressDTOs = addresses.stream().map(address -> modelMapper.map(address, AddressDTO.class))
				.collect(Collectors.toList());

		return addressDTOs;
	}

	@Override
	public AddressDTO getAddress(Long addressId) {
		Address address = addressRepo.findById(addressId)
				.orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

		return modelMapper.map(address, AddressDTO.class);
	}

	@Override
	public AddressDTO updateAddress(Long addressId, Address address) {
		Address addressFromDB = addressRepo.findByStreetAndBuildingAndCityAndProvinceAndCountryAndPincode(
				address.getStreet(), address.getBuilding(), address.getCity(), address.getProvince(), address.getCountry(),
				address.getPincode());

		if (addressFromDB == null) {
			addressFromDB = addressRepo.findById(addressId)
					.orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

			addressFromDB.setStreet(address.getStreet());
			addressFromDB.setBuilding(address.getBuilding());
			addressFromDB.setCity(address.getCity());
			addressFromDB.setProvince(address.getProvince());
			addressFromDB.setCountry(address.getCountry());
			addressFromDB.setPincode(address.getPincode());
			
			
			Address updatedAddress = addressRepo.save(addressFromDB);

			return modelMapper.map(updatedAddress, AddressDTO.class);
		} else {
			List<User> users = userRepo.findByAddresses_AddressId(addressId);
			final Address a = addressFromDB;

			users.forEach(user -> user.getAddresses().add(a));

			deleteAddress(addressId);

			return modelMapper.map(addressFromDB, AddressDTO.class);
		}
	}

	@Override
	public String deleteAddress(Long addressId) {
		Address addressFromDB = addressRepo.findById(addressId)
				.orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

		List<User> users = userRepo.findByAddresses_AddressId(addressId);

		users.forEach(user -> {
			user.getAddresses().remove(addressFromDB);

			userRepo.save(user);
		});

		addressRepo.deleteById(addressId);

		return "Address deleted succesfully with addressId: " + addressId;
	}

}
