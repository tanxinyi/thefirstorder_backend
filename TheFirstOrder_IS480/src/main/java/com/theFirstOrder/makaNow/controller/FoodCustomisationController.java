package com.theFirstOrder.makaNow.controller;

import com.theFirstOrder.makaNow.model.FoodCustomisation;
import com.theFirstOrder.makaNow.service.FoodCustomisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class FoodCustomisationController {

    @Autowired
    private FoodCustomisationService foodCustomisationService;

    @RequestMapping(value="/api/table/getAllFoodCustomisations", method = RequestMethod.GET)
    public List<FoodCustomisation> getAllFoodCustomisations(){
        return foodCustomisationService.getAllFoodCustomisations();
    }

    @RequestMapping(value="/api/table/getFoodCustomisationsByFoodId/{foodId}", method = RequestMethod.GET)
    public List<FoodCustomisation> getAllFoodCustomisations( @PathVariable String foodId ){
        return foodCustomisationService.getFoodCustomisationByFoodId(foodId);
    }
}
