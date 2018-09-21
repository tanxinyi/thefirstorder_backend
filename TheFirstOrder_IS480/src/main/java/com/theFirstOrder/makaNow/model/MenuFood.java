package com.theFirstOrder.makaNow.model;

import lombok.Data;

import java.util.*;

@Data
public class MenuFood {

    private String foodId;
    private String foodName;
    private String foodDescription;
    private String foodCategory;
    private double foodPrice;
    private List<List<Customisation>> customisations;

    public MenuFood(String foodId, String foodName, String foodDescription, String foodCategory, double foodPrice, List<List<Customisation>> customisations){
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodDescription = foodDescription;
        this.foodCategory = foodCategory;
        this.foodPrice = foodPrice;
        this.customisations = customisations;
    }

    public MenuFood(String foodId, String foodName, String foodDescription, String foodCategory, double foodPrice){
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodDescription = foodDescription;
        this.foodCategory = foodCategory;
        this.foodPrice = foodPrice;
    }
}