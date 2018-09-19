package com.theFirstOrder.makaNow.model;

import com.theFirstOrder.makaNow.model.FoodItem;
import lombok.Data;

@Data
public class OrderItem {
    private static int currentCount = 0;
    private int id;
    private FoodItem foodItem;
    private int quantity;
    private double price;
    private String remark;
}
