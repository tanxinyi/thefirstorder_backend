package com.theFirstOrder.makaNow.model;

import lombok.Data;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.HashMap;


@Data
public class Restaurant {
    @Id
    private String restaurantId;
    private String name;
    private String description;
    private String location;
    private String contact;
    private HashMap<String,ArrayList<String>> openingHours;

}
