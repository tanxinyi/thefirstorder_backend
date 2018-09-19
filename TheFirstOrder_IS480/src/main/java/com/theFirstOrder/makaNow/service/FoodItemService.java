package com.theFirstOrder.makaNow.service;

import com.theFirstOrder.makaNow.model.FoodItem;
import com.theFirstOrder.makaNow.repository.FoodItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FoodItemService {

    @Autowired
    private FoodItemRepository foodItemRepository;

    public List<FoodItem> getAllFoodItems(){
        List<FoodItem> foodItems = new ArrayList<>();
        foodItemRepository.findAll()
                .forEach(foodItems::add);
        return foodItems;
    }
}
