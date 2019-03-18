package MakaNow.thefirstorder_back.service;

import MakaNow.thefirstorder_back.model.ManagerAllocation;
import MakaNow.thefirstorder_back.model.Restaurant;
import MakaNow.thefirstorder_back.repository.RestaurantRepository;
import ch.qos.logback.core.pattern.util.RestrictedEscapeUtil;
import javassist.NotFoundException;
import org.aspectj.weaver.ast.Not;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RestaurantService {

    private final List<Integer> POSTAL_WEST = Arrays.asList(11,12,13,60,61,62,63,64,65,66,67,68);
    private final List<Integer> POSTAL_NORTH = Arrays.asList(69,70,71,72,73,75,76);
    private final List<Integer> POSTAL_NORTHEAST = Arrays.asList(53,54,55,56,57,79,80,82);
    private final List<Integer> POSTAL_EAST = Arrays.asList(42,43,44,45,46,47,48,49,50,51,52,81);

    Logger logger = LoggerFactory.getLogger(RestaurantService.class);

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

    public String getNewRestaurantId() {
        List<String> restaurantIds = new ArrayList<>();
        List<Restaurant> restaurants = (List<Restaurant>) restaurantRepository.findAll();

        //For first menu created to avoid index out of bounds error
        if (restaurants.size() == 0) {
            return "R001";
        }

        for (int i = 0; i < restaurants.size(); i++) {
            Restaurant restaurant = restaurants.get(i);
            String restaurantId = restaurant.getRestaurantId();
            restaurantIds.add(restaurantId);
        }
        Collections.sort(restaurantIds);
        String lastRestaurantId = restaurantIds.get(restaurantIds.size() - 1);
        int restaurantIdNumber = Integer.parseInt(lastRestaurantId.substring(1));
        int newRestaurantIdNumber = restaurantIdNumber + 1;
        int length = String.valueOf(newRestaurantIdNumber).length();

        if (length == 1) {
            return ("R00" + newRestaurantIdNumber);
        } else if (length == 2) {
            return ("R0" + newRestaurantIdNumber);
        }
        return ("R" + newRestaurantIdNumber);
    }

    public List<Restaurant> getRestaurantsByArea(String area){
        List<Restaurant> restaurants = (List<Restaurant>) restaurantRepository.findAll();
        List<Restaurant> output = new ArrayList<>();

        for(Restaurant restaurant: restaurants) {
            int postal = Integer.parseInt(restaurant.getPostalCode().substring(0,2));
            switch (area) {
                case "NORTH":
                    if(POSTAL_NORTH.contains(postal)) output.add(restaurant);
                    break;
                case "NORTHEAST":
                    if(POSTAL_NORTHEAST.contains(postal)) output.add(restaurant);
                    break;
                case "WEST":
                    if(POSTAL_WEST.contains(postal)) output.add(restaurant);
                    break;
                case "EAST":
                    if(POSTAL_EAST.contains(postal)) output.add(restaurant);
                    break;
                case "CENTRAL":
                    if(postal > 0 && postal < 83
                    && !POSTAL_NORTH.contains(postal)
                    && !POSTAL_NORTHEAST.contains(postal)
                    && !POSTAL_WEST.contains(postal)
                    && !POSTAL_EAST.contains(postal)) output.add(restaurant);
                    break;
                default:
                    boolean street = restaurant.getStreet().indexOf(area) != -1;
                    boolean postalCode = restaurant.getPostalCode().indexOf(area) != -1;
                    boolean building = restaurant.getBuilding().indexOf(area) != -1;
                    if(street || postalCode || building) output.add(restaurant);
            }
        }
        return output;
    }

    public List<Restaurant> getRestaurantsByCuisine(String cuisine) throws NotFoundException{
        List<Restaurant> restaurants = (List<Restaurant>)restaurantRepository.findAll();
        List<Restaurant> output = new ArrayList<>();
        for(Restaurant restaurant: restaurants){
            if(restaurant.getCuisine().indexOf(cuisine) != -1){
                output.add(restaurant);
            }
        }
        if(output.size() > 0){ return output;}
        throw new NotFoundException("Cuisine not found");
    }

    public List<String> getListOfCuisines(){
        List<Restaurant> restaurants = (List<Restaurant>)restaurantRepository.findAll();
        List<String> output = new ArrayList<>();
        for(Restaurant restaurant: restaurants){
            String cuisine = restaurant.getCuisine();
            if(!output.contains(cuisine)) output.add(cuisine);
        }

        return output;
    }

    public List<Restaurant> getRestaurantsByPriceRange(String priceRange) throws NotFoundException{
        List<Restaurant> restaurants = (List<Restaurant>)restaurantRepository.findAll();
        List<Restaurant> output = new ArrayList<>();
        for(Restaurant restaurant: restaurants){
            if(restaurant.getRestaurantPriceRange().equals(priceRange)){
                output.add(restaurant);
            }
        }
        if(output.size() > 0){ return output;}
        throw new NotFoundException("Price range not found");
    }

    public List<String> getPriceRanges(){
        List<Restaurant> restaurants = (List<Restaurant>)restaurantRepository.findAll();
        List<String> output = new ArrayList<>();
        for(Restaurant restaurant: restaurants){
            String priceRange = restaurant.getRestaurantPriceRange();
            if(!output.contains(priceRange)) output.add(priceRange);
        }

        return output;
    }

    public List<Restaurant> getRestaurantsByName(String name) throws NotFoundException {
        List<Restaurant> restaurants = (List<Restaurant>) restaurantRepository.findAll();
        List<Restaurant> output = new ArrayList<>();

        for(Restaurant restaurant: restaurants){
            if(restaurant.getRestaurantName().indexOf(name) != -1){
                output.add(restaurant);
            }
        }
        if(output.size() > 0) return output;
        throw new NotFoundException("No restaurant of name " + name + " is found.");
    }

    public List<Restaurant> queryRestaurant(String query) throws NotFoundException {
        List<Restaurant> restaurants = (List<Restaurant>) restaurantRepository.findAll();
        List<Restaurant> output = new ArrayList<>();
        query = query.toLowerCase();

        for(Restaurant restaurant: restaurants){
            boolean description = restaurant.getRestaurantDescription().toLowerCase().indexOf(query) != -1;
            boolean name = restaurant.getRestaurantName().toLowerCase().indexOf(query) != -1;
            boolean area =  restaurant.getStreet().toLowerCase().indexOf(query) != -1 || restaurant.getPostalCode().toLowerCase().indexOf(query) != -1 || restaurant.getBuilding().toLowerCase().indexOf(query) != -1;
            boolean cuisine = restaurant.getCuisine().toLowerCase().indexOf(query) != -1;

            if(description || name || area || cuisine){
                logger.info("Restaurant: " + restaurant.getRestaurantName());
                logger.info("description: " + description);
                logger.info("name: " + name);
                logger.info("area: " + area);
                logger.info("cuisine: " + cuisine);
                output.add(restaurant);
            }
        }
        if(output.size() > 0) return output;
        throw new NotFoundException("No restaurants match found");
    }
}
