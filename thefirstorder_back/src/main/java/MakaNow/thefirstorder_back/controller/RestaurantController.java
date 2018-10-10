package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.Restaurant;
import MakaNow.thefirstorder_back.model.View;
import MakaNow.thefirstorder_back.repository.RestaurantRepository;
import com.fasterxml.jackson.annotation.JsonView;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api")
public class RestaurantController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @GetMapping("/restaurants")
    public List<Restaurant> getAllRestaurants(){
        return (List<Restaurant>) restaurantRepository.findAll();
    }

    @GetMapping("/restaurants/{restaurantId}")
    @JsonView(View.ViewB.class)
    public Restaurant getRestaurantById( @PathVariable String restaurantId ) throws NotFoundException {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
        if(optionalRestaurant.isPresent()){
            return optionalRestaurant.get();
        }else{
            throw new NotFoundException("Menu ID:" + restaurantId + " does not exist");
        }
    }

    @PostMapping("/restaurants")
    public Restaurant createRestaurant(@Valid @RequestBody Restaurant restaurant){
        return restaurantRepository.save(restaurant);
    }

    @PutMapping("/restaurants/{restaurantId}")
    public Restaurant updateRestaurant(@PathVariable String restaurantId,
                                       @Valid @RequestBody Restaurant restaurantUpdated) throws NotFoundException {
        return restaurantRepository.findById(restaurantId)
            .map(restaurant -> {
                restaurant.setName(restaurantUpdated.getName());
                restaurant.setDescription(restaurantUpdated.getDescription());
                restaurant.setContactNumber(restaurantUpdated.getContactNumber());
                restaurant.setStreet(restaurantUpdated.getStreet());
                restaurant.setPostalCode(restaurantUpdated.getPostalCode());
                restaurant.setCuisine(restaurantUpdated.getCuisine());
                return restaurantRepository.save(restaurant);
            }).orElseThrow(() -> new NotFoundException("Restaurant not found with ID " + restaurantId));
    }

    @DeleteMapping("/restaurants/{restaurantId}")
    public String deleteRestaurant(@PathVariable String restaurantId) throws NotFoundException {
        return restaurantRepository.findById(restaurantId)
                .map(restaurant -> {
                    restaurantRepository.delete(restaurant);
                    return "Delete Successfully";
                }).orElseThrow(() -> new NotFoundException("Restaurant item not found with Food ID: " + restaurantId));
    }
}
