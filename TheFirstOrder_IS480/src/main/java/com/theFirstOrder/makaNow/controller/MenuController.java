package com.theFirstOrder.makaNow.controller;

import com.theFirstOrder.makaNow.model.*;
import com.theFirstOrder.makaNow.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class MenuController {

    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuPriceService menuPriceService;
    @Autowired
    private FoodService foodService;
    @Autowired
    private FoodCustomisationService foodCustomisationService;
    @Autowired
    private CustomisationService customisationService;

    @RequestMapping(value="/api/table/getAllMenus", method = RequestMethod.GET)
    public List<Menu> getAllMenus(){
        return menuService.getAllMenus();
    }

    @RequestMapping(value="/api/menu/getMenuByRestaurantId", params = "restaurantId", method = RequestMethod.GET)
    public Menu getMenu(@RequestParam("restaurantId") String restaurantId){ return menuService.getMenuByRestaurantId(restaurantId); }

    @RequestMapping(value="/api/menu/viewMenuItems", params = "menuId", method = RequestMethod.GET)
    public List<MenuFood> viewMenuItems(@RequestParam("menuId") String menuId) {

        Food food;
        MenuFood menuFood = null;
        List<MenuPrice> menuPrices;
        List<MenuFood> menuFoodList = new ArrayList<>();

        //Get menu prices of restaurant
        menuPrices = menuPriceService.viewSpecificMenu(menuId);
        for (int i = 0; i < menuPrices.size(); i++){
            MenuPrice menuPrice = menuPrices.get(i);
            String foodId = menuPrice.getMenuPricePK().getFoodId();

            //Get food item
            food = foodService.getFoodByFoodId(foodId);

            //Get food customisations
            List<FoodCustomisation> specificFoodCustomisation = foodCustomisationService.getFoodCustomisationByFoodId(foodId);
            if (specificFoodCustomisation.isEmpty()){
                menuFood = new MenuFood(foodId, food.getName(), food.getDescription(), food.getCategory(), menuPrice.getPrice(), null);
            }else {
                List<Customisation> customisations;
                List<List<Customisation>> customisationsList = new ArrayList<>();
                for (int j = 0; j < specificFoodCustomisation.size(); j++) {
                    FoodCustomisation foodCustomisation = specificFoodCustomisation.get(j);
                    String customisationId = foodCustomisation.getFoodCustomisationPK().getCustomisationId1();
                    customisations = customisationService.getCustomisationsByCustomisationId(customisationId);
                    customisationsList.add(customisations);
                    menuFood = new MenuFood(foodId, food.getName(), food.getDescription(), food.getCategory(), menuPrice.getPrice(), customisationsList);
                }
            }
            menuFoodList.add(menuFood);
        }

        return menuFoodList;

    }
}
