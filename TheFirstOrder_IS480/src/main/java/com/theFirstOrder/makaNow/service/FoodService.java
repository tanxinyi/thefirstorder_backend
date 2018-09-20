package com.theFirstOrder.makaNow.service;

import com.theFirstOrder.makaNow.model.Food;
import com.theFirstOrder.makaNow.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;

    public List<Food> getAllFoodItems(){
        List<Food> foodItems = new ArrayList<>();
        foodRepository.findAll()
                .forEach(foodItems::add);
        return foodItems;
    }
}
