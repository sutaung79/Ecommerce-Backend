package com.ecom.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

	private Long addressId;
	private String street;
	private String building;
	private String city;
	private String province;
	private String country;
	private String pincode;
}
