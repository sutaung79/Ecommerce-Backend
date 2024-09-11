package com.ecom.app.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_seq")
    @SequenceGenerator(name = "address_seq", sequenceName = "address_seq", allocationSize = 1)
    private Long addressId;

    @NotBlank(message = "Street name is required.")
    private String street;
    
    @NotBlank(message = "Building name is required.")
    private String building;
    
    @NotBlank(message = "City name is required.")
    private String city;
    
    @NotBlank(message = "Province name is required.")
    private String province;
    
    @NotBlank(message = "Country name is required.")
    private String country;
    
    @NotBlank(message = "Pincode is required")
    private String pincode;

    @ManyToMany(mappedBy = "addresses", fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();

    public Address( String street,String building, String city, String province, String country,   String pincode) {
              
        this.street = street;
        this.building = building;
        this.province = province;
        this.city = city;
        this.country = country;
        this.pincode = pincode;
    }
}

