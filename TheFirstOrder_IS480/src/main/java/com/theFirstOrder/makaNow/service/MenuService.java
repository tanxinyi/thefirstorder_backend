package com.theFirstOrder.makaNow.service;

import com.theFirstOrder.makaNow.model.Menu;
import com.theFirstOrder.makaNow.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    public List<Menu> getAllMenus(){
        List<Menu> menus = new ArrayList<>();
        menuRepository.findAll()
                .forEach(menus::add);
        return menus;
    }

    public List<Menu> getMenuByRestaurantId(String restaurantId){
        List<Menu> restaurantMenus = new ArrayList<>();
        List<Menu> menus = getAllMenus();
        for (int i = 0; i < menus.size(); i++){
            Menu menu = menus.get(i);
            if (menu.getMenuPK().getRestaurantId().equals(restaurantId)){
                restaurantMenus.add(menu);
            }
        }
        return restaurantMenus;
    }
}
