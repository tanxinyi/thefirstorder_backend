package com.theFirstOrder.makaNow.model;

import com.theFirstOrder.makaNow.model.Food;
import lombok.Data;

@Data
public class OrderItem {
    private static int currentCount = 0;
    private int id;
    private Food food;
    private int quantity;
    private double price;
    private String remark;
}
