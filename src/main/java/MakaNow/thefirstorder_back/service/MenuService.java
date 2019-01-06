package MakaNow.thefirstorder_back.service;

import MakaNow.thefirstorder_back.model.Menu;
import MakaNow.thefirstorder_back.model.Restaurant;
import MakaNow.thefirstorder_back.repository.MenuRepository;
import MakaNow.thefirstorder_back.repository.RestaurantRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.*;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    public Menu getMenuByMenuId(String menuId){
        Optional<Menu> optionalMenu = menuRepository.findById(menuId);
        if(optionalMenu.isPresent()){
            Menu menu = optionalMenu.get();
            return menu;
        }else{
            return null;
        }
    }

    public List<Menu> getMenusByRestaurantId(String restaurantId){
        List<Menu> results = new ArrayList<>();
        List<Restaurant> restaurants = (List<Restaurant>) restaurantRepository.findAll();
        List<Menu> menus;
        for (int i = 0; i < restaurants.size(); i++){
            Restaurant restaurant = restaurants.get(i);
            menus = restaurant.getMenus();
            for (int j = 0; j < menus.size(); j++){
                Menu menu = menus.get(j);
                if (menu.getRestaurantId().equals(restaurantId)){
                    results.add(menu);
                }
            }
        }
        return results;
    }

    public String getNewMenuId(){
        List<String> menuIds = new ArrayList<>();
        List<Menu> menus = (List<Menu>) menuRepository.findAll();

        //For first menu created to avoid index out of bounds error
        if(menus.size() == 0){
            return "M001";
        }

        for(int i = 0; i < menus.size(); i++){
            Menu menu = menus.get(i);
            String menuId = menu.getMenuId();
            menuIds.add(menuId);
        }
        Collections.sort(menuIds);
        String lastMenuId = menuIds.get(menuIds.size()-1);
        int menuIdNumber = Integer.parseInt(lastMenuId.substring(1));
        int newMenuIdNumber = menuIdNumber + 1;
        int length = String.valueOf(newMenuIdNumber).length();

        if(length == 1) {
            return ("M00" + newMenuIdNumber);
        }
        else if(length == 2){
            return ("M0" + newMenuIdNumber);
        }
        return ("M" + newMenuIdNumber);
    }
}
