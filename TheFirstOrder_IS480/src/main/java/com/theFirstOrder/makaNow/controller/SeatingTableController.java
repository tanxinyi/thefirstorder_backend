package com.theFirstOrder.makaNow.controller;


import com.theFirstOrder.makaNow.model.*;
import com.theFirstOrder.makaNow.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class SeatingTableController {

    @Autowired
    private SeatingTableService seatingTableService;
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

    @RequestMapping(value="/api/table/getAllTables", method = RequestMethod.GET)
    public List<SeatingTable> getAllSeatingTables(){
        return seatingTableService.getAllSeatingTables();
    }

    @RequestMapping(value="/api/table/registerTable/{qrCode}", method = RequestMethod.GET)
    public List<MenuFood> registerTable(@PathVariable String qrCode){
        //Receives Qr Code and check against database, if Qr Code matches, register table and create empty order
        String orderId = "";
        Order order = null;
        Menu menu = null;
        Food food = null;
        MenuFood menuFood = null;
        List<MenuPrice> menuPrices = new ArrayList<>();
        List<MenuFood> menuFoodList = new ArrayList<>();

        SeatingTable seatingTable = seatingTableService.getSeatingTableByQrCode(qrCode);

        if (seatingTable == null){
            System.out.print("QR Code Scan Error. Please Try Again!");
            return null;
        }
        if (seatingTable != null){
            //Concatenate restaurant id, table id and date time now to generate order id and create an order object
            String restaurantId = seatingTable.getSeatingTablePK().getRestaurantId();
            String tableId = seatingTable.getSeatingTablePK().getTableId();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMddhhmm");
            LocalDateTime now = LocalDateTime.now();
            orderId = "O" + restaurantId.substring(1) + tableId.substring(1) + dtf.format(now);
            order = new Order(orderId, "NOT SENT");

            //Get menu of restaurant
            menu = menuService.getMenuByRestaurantId(restaurantId);
            String menuId = menu.getMenuPK().getMenuId();

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
        }
        return menuFoodList;
    }
}