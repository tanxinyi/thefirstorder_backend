package com.theFirstOrder.makaNow.service;

import com.theFirstOrder.makaNow.model.Food;
import com.theFirstOrder.makaNow.model.FoodCustomisation;
import com.theFirstOrder.makaNow.repository.FoodCustomisationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FoodCustomisationService {

    @Autowired
    private FoodCustomisationRepository foodCustomisationRepository;

    public List<FoodCustomisation> getAllFoodCustomisations(){
        List<FoodCustomisation> foodCustomisations = new ArrayList<>();
        foodCustomisationRepository.findAll()
                .forEach(foodCustomisations::add);
        return foodCustomisations;
    }

    public List<FoodCustomisation> getFoodCustomisationByFoodId(String foodId){
        List<FoodCustomisation> foodCustomisations = getAllFoodCustomisations();
        List<FoodCustomisation> specificFoodCustomisation = new ArrayList<>();
        for (int i = 0; i < foodCustomisations.size(); i++){
            FoodCustomisation foodCustomisation = foodCustomisations.get(i);
            if (foodCustomisation.getFoodCustomisationPK().getFoodId().equals(foodId)){
                specificFoodCustomisation.add(foodCustomisation);
            }
        }
        return specificFoodCustomisation;
    }
}
