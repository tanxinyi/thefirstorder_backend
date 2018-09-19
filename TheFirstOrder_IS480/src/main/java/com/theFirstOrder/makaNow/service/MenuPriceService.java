package com.theFirstOrder.makaNow.service;

import com.theFirstOrder.makaNow.model.MenuPrice;
import com.theFirstOrder.makaNow.repository.MenuPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MenuPriceService {

    @Autowired
    private MenuPriceRepository menuPriceRepository;

    public List<MenuPrice> getAllMenuPrices(){
        List<MenuPrice> menuPrices = new ArrayList<>();
        menuPriceRepository.findAll()
                .forEach(menuPrices::add);
        return menuPrices;
    }

    public List<MenuPrice> viewSpecificMenu(String menuId){
        List<MenuPrice> specificMenuPrices = new ArrayList<>();
        List<MenuPrice> menuPrices = getAllMenuPrices();
        for(int i = 0; i < menuPrices.size(); i++){
            MenuPrice menuPrice = menuPrices.get(i);
            if (menuPrice.getMenuPricePK().getMenuId().equals(menuId)){
                specificMenuPrices.add(menuPrice);
            }
        }
        return specificMenuPrices;
    }
}
