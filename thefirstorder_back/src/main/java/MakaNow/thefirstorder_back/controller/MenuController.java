package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.Menu;
import MakaNow.thefirstorder_back.model.Restaurant;
import MakaNow.thefirstorder_back.model.View;
import MakaNow.thefirstorder_back.repository.MenuRepository;
import MakaNow.thefirstorder_back.repository.RestaurantRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api")
public class MenuController {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @GetMapping("/menus")
    public List<Menu> getAllMenus(){
        return (List<Menu>) menuRepository.findAll();
    }

    @GetMapping("/menus/{menuId}")
    public Menu getMenuById( @PathVariable String menuId ) throws NotFoundException {
        Optional<Menu> optionalMenu = menuRepository.findById(menuId);
        if(optionalMenu.isPresent()){
            return optionalMenu.get();
        }else{
            throw new NotFoundException("Menu ID:" + menuId + " does not exist");
        }
    }

    @GetMapping("/restaurants/{restaurantId}/menu")
    public Menu getLatestMenuByRestaurant( @PathVariable String restaurantId) throws NotFoundException {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
        if(optionalRestaurant.isPresent()){
            List<Menu> menus = optionalRestaurant.get().getMenus();
            Collections.sort(menus);
            return menus.get(0);
        }else{
            throw new NotFoundException("Restaurant ID:" + restaurantId + " does not exist");
        }
    }


    @PostMapping("/restaurants/{restaurantId}/menus")
    @JsonView(View.ViewA.class)
    public Menu addMenu(@PathVariable String restaurantId,
                                        @Valid @RequestBody Menu menu) throws NotFoundException {
        return restaurantRepository.findById(restaurantId)
                .map(restaurant -> {
                    menu.setRestaurant(restaurant);
                    return menuRepository.save(menu);
                }).orElseThrow(()-> new NotFoundException("Restaurant Not Found."));

    }

    @PutMapping("/restaurants/{restaurantId}/Menus/{menuId}")
    public Menu updateMenu(@PathVariable String restaurantId,
                                           @PathVariable String menuId,
                                           @Valid @RequestBody Menu menuUpdated) throws NotFoundException {
        if(!restaurantRepository.existsById(restaurantId)){
            throw new NotFoundException("Restaurant Not Found.");
        }

        return menuRepository.findById(menuId)
                .map(menu -> {
                    menu.setDateOfCreation(menuUpdated.getDateOfCreation());
                    return menuRepository.save(menu);
                }).orElseThrow(()-> new NotFoundException("Update Unsuccessful."));
    }
}
