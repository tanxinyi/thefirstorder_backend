package com.theFirstOrder.makaNow.controller;

import com.theFirstOrder.makaNow.model.Menu;
import com.theFirstOrder.makaNow.model.MenuPrice;
import com.theFirstOrder.makaNow.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class MenuController {

    @Autowired
    private MenuService menuService;

    @RequestMapping(value="/api/table/getAllMenus", method = RequestMethod.GET)
    public List<Menu> getAllMenus(){
        return menuService.getAllMenus();
    }

    @RequestMapping(value="/api/table/viewMenu/{restaurantId}", method = RequestMethod.GET)
    public Menu viewMenu( @PathVariable String restaurantId) {
        return menuService.getMenuByRestaurantId(restaurantId);
    }
}
