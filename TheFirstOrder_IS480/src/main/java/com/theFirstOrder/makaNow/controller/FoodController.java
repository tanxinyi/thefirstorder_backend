package com.theFirstOrder.makaNow.controller;

import com.theFirstOrder.makaNow.model.Food;
import com.theFirstOrder.makaNow.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class FoodController {

    @Autowired
    private FoodService foodService;

    @RequestMapping(value="/api/table/getAllFoodItems", method = RequestMethod.GET)
    public List<Food> getAllFoodItems(){
        return foodService.getAllFoodItems();
    }

}
