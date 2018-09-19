package com.agogo.restaurantpos.controller;

import com.agogo.restaurantpos.model.Restaurant;
import org.springframework.web.bind.annotation.*;


@RestController
public class RestaurantController {
    //Needs to be able to register restaurant

    //Get all the details of a restaurant
    @RequestMapping(value = "/api/restaurant/getRestaurant/{id}", method = RequestMethod.GET)
    public Restaurant getRestaurantDetails(@PathVariable int id){
        return null;
    }

    @RequestMapping(value = "/api/restaurant/addRestaurant", method = RequestMethod.POST)
    public void addRestaurantDetails(@RequestBody Restaurant restaurant){

    }
}
