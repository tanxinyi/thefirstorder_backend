package com.theFirstOrder.makaNow.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.*;

@Entity
@Data
public class Food {

    @Id
    @Column(name = "food_id")
    private String foodId;
    @Column(name = "food_name")
    private String name;
    @Column(name="food_desc")
    private String description;
    @Column(name="food_category")
    private String category;

//    public Food(){

//    }

//    public Food(String foodId, String name, String description, String category){
//        this.foodId = foodId;
////        this.name = name;
////        this.description = description;
////        this.category = category;
//
}

