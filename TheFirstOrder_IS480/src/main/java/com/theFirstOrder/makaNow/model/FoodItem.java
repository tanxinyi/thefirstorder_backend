package com.theFirstOrder.makaNow.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.*;

@Entity
@Data
public class FoodItem {

    @Id
    private String foodId;
    private String name;
    private String description;
    private String category;

    public FoodItem(){

    }

    public FoodItem(String foodId, String name, String description, String category){
        this.foodId = foodId;
        this.name = name;
        this.description = description;
        this.category = category;
    }
}
