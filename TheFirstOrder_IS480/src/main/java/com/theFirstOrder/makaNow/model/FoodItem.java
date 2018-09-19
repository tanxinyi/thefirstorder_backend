package com.agogo.restaurantpos.model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class FoodItem {
    private String foodId;
    private String name;
    private String description;
    private String category;
    private ArrayList<String> tags;
}
