package com.theFirstOrder.makaNow.controller;

import com.theFirstOrder.makaNow.model.OrderItem;
import com.theFirstOrder.makaNow.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    @Autowired
    OrderService orderService;

    @RequestMapping(value="/api/order/get", method = RequestMethod.GET)
    public void getCurrentOrder(){
        //Gets the current order list
    }

    @RequestMapping(value = "/api/order/add", method = RequestMethod.PUT)
    public void addToCurrentOrder(@RequestBody OrderItem orderItem){
        //Adds to current order or creates one if it doesn't already exist

    }

}
