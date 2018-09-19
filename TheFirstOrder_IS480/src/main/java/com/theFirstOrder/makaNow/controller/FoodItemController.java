package com.theFirstOrder.makaNow.controller;

import com.theFirstOrder.makaNow.model.FoodItem;
import com.theFirstOrder.makaNow.service.FoodItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class FoodItemController {

    @Autowired
    private FoodItemService foodItemService;

    @RequestMapping(value="/api/table/getAllFoodItems", method = RequestMethod.GET)
    public List<FoodItem> getAllFoodItems(){
        return foodItemService.getAllFoodItems();
    }

}
