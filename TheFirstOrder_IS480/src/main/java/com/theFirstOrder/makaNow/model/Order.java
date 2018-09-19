package com.theFirstOrder.makaNow.model;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.*;

@Entity
@Data
public class Order {

    @Id
    private String orderId;
    private String orderStatus;
    HashMap<MenuPrice, Integer> menuPriceAndQuantityList;
    private double totalPrice;

    public Order(String orderId, String orderStatus){
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        menuPriceAndQuantityList = new HashMap<>();
        totalPrice = 0;
    }
}
