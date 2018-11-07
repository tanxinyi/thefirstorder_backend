package MakaNow.thefirstorder_back.service;

import MakaNow.thefirstorder_back.model.ManagerAllocation;
import MakaNow.thefirstorder_back.model.Restaurant;
import MakaNow.thefirstorder_back.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    public List<Restaurant> getRestaurantsAllocatedByManagerId(String managerId){
        List<Restaurant> results = new ArrayList<>();
        List<Restaurant> restaurants = (List<Restaurant>) restaurantRepository.findAll();
        List<ManagerAllocation> managerAllocations;
        for (int i = 0; i < restaurants.size(); i++){
            Restaurant restaurant = restaurants.get(i);
            managerAllocations = restaurant.getManagerAllocations();
            for (int j = 0; j < managerAllocations.size(); j++){
                ManagerAllocation managerAllocation = managerAllocations.get(j);
                if (managerAllocation.getManagerAllocationPK().getManagerId().equals(managerId)){
                    results.add(restaurant);
                }
            }
        }
        return results;
    }
}
