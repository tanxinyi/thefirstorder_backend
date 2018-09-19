package com.theFirstOrder.makaNow.controller;

import com.theFirstOrder.makaNow.model.MenuPrice;
import com.theFirstOrder.makaNow.service.MenuPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MenuPriceController {

    @Autowired
    private MenuPriceService menuPriceService;

    @RequestMapping(value="/api/table/getAllMenuPrices", method = RequestMethod.GET)
    public List<MenuPrice> getAllSeatingTables(){
        return menuPriceService.getAllMenuPrices();
    }

    @RequestMapping(value="/api/table/viewMenuItemsOfRestaurant/{menuId}", method = RequestMethod.GET)
    public List<MenuPrice> viewMenuItemsOfRestaurant( @PathVariable String menuId){
        List<MenuPrice> menuPrices = menuPriceService.viewSpecificMenu(menuId);
        return menuPrices;
    }
}
