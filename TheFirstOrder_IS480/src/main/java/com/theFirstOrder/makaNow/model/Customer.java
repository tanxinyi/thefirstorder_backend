package com.agogo.restaurantpos.model;

import lombok.Data;

@Data
public class Customer {
    private String customerId;
    private String password;
    private String name;
    private int age;
    private char gender;
    private String address;
    private String email;
    private String contactNo;
    private long loyaltyPoints;

}

