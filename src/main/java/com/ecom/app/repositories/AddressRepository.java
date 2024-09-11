package com.ecom.app.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecom.app.entities.Address;


@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {



	Address findByStreetAndBuildingAndCityAndProvinceAndCountryAndPincode(
			String street, 
			String building, 
			String city,
			String province,
			String country, 
			String pincode);

	
}

