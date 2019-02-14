package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.Restaurant;
import MakaNow.thefirstorder_back.model.View;
import MakaNow.thefirstorder_back.repository.RestaurantRepository;
import MakaNow.thefirstorder_back.service.RestaurantService;
import com.fasterxml.jackson.annotation.JsonView;
import javassist.NotFoundException;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class RestaurantController {

    Logger logger = LoggerFactory.getLogger(RestaurantController.class);

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/restaurants")
    @JsonView(View.RestaurantView.class)
    public List<Restaurant> getAllRestaurants(){
        return (List<Restaurant>) restaurantRepository.findAll();
    }

    @GetMapping("/restaurants/{restaurantId}")
    @JsonView(View.RestaurantView.class)
    public Restaurant getRestaurantById( @PathVariable String restaurantId ) throws NotFoundException {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
        if(optionalRestaurant.isPresent()){
            return optionalRestaurant.get();
        }else{
            throw new NotFoundException("Menu ID does not exist");
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
                restaurant.setRestaurantName(restaurantUpdated.getRestaurantName());
                restaurant.setRestaurantDescription(restaurantUpdated.getRestaurantDescription());
                restaurant.setRestaurantContactNumber(restaurantUpdated.getRestaurantContactNumber());
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
                    return "Restaurant Deleted Successfully";
                }).orElseThrow(() -> new NotFoundException("Restaurant item not found with Food ID: " + restaurantId));
    }

    @GetMapping("/restaurants/getRestaurantsByManagerId/{managerId}")
    @JsonView(View.RestaurantView.class)
    public ResponseEntity<?> getRestaurantsAllocatedByManagerId( @PathVariable("managerId") String managerId ){
        List<Restaurant> result;
        result = restaurantService.getRestaurantsAllocatedByManagerId(managerId);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @PostMapping("/restaurants/updateRestaurant/{restaurantId}")
    public ResponseEntity<?> updateRestaurantDetails(@PathVariable("restaurantId") String restaurantId, @RequestBody UpdatedRestaurant updatedRestaurant) {

        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);

        if(optionalRestaurant.isPresent()){
            Restaurant restaurant = optionalRestaurant.get();
            restaurant.setRestaurantName(updatedRestaurant.getRestaurantName());
            restaurant.setRestaurantDescription(updatedRestaurant.getRestaurantDescription());
            restaurant.setRestaurantContactNumber(updatedRestaurant.getContactNumber());
            restaurant.setBuilding(updatedRestaurant.getBuilding());
            restaurant.setStreet(updatedRestaurant.getStreet());
            restaurant.setPostalCode(updatedRestaurant.getPostalCode());
            restaurant.setCuisine(updatedRestaurant.getCuisine());
            restaurant.setRestaurantOpeningHours(updatedRestaurant.getOperatingHours());
            restaurant.setRestaurantPriceRange(updatedRestaurant.getAffordability());
            String restaurantImg = updatedRestaurant.getRestaurantImg();
            byte[] restaurantImgByte = restaurantImg.getBytes();
            restaurant.setRestaurantImgPath(restaurantImgByte);

            restaurantRepository.save(restaurant);
        }
        return new ResponseEntity("Food Item updated successfully", HttpStatus.OK);
    }

    @PostMapping("/restaurant/addRestaurant/{adminId}")
    @JsonView(View.FoodPriceView.class)
    public ResponseEntity<?> addRestaurant(@PathVariable("adminId") String adminId, @RequestBody UpdatedRestaurant updatedRestaurant) {

        String restaurantId = restaurantService.getNewRestaurantId();
        String restaurantName = updatedRestaurant.getRestaurantName();
        String restaurantDescription = updatedRestaurant.getRestaurantDescription();
        String contactNumber = updatedRestaurant.getContactNumber();
        String building = updatedRestaurant.getBuilding();
        String street = updatedRestaurant.getStreet();
        String postalCode = updatedRestaurant.getPostalCode();
        String cuisine = updatedRestaurant.getCuisine();
        String operatingHours = updatedRestaurant.getOperatingHours();
        String affordability = updatedRestaurant.getAffordability();
        String restaurantImg = updatedRestaurant.getRestaurantImg();

        byte[] restaurantImgByte = restaurantImg.getBytes();

        Restaurant newRestaurant = new Restaurant();
        newRestaurant.setAdminId(adminId);
        newRestaurant.setRestaurantId(restaurantId);
        newRestaurant.setRestaurantName(restaurantName);
        newRestaurant.setRestaurantDescription(restaurantDescription);
        newRestaurant.setRestaurantContactNumber(contactNumber);
        newRestaurant.setBuilding(building);
        newRestaurant.setStreet(street);
        newRestaurant.setPostalCode(postalCode);
        newRestaurant.setCuisine(cuisine);
        newRestaurant.setRestaurantOpeningHours(operatingHours);
        newRestaurant.setRestaurantPriceRange(affordability);
        newRestaurant.setRestaurantImgPath(restaurantImgByte);

        restaurantRepository.save(newRestaurant);

        return new ResponseEntity("Restaurant added successfully", HttpStatus.OK);
    }

    @GetMapping("/restaurants/getRestaurantsByArea/{area}")
    @JsonView(View.RestaurantView.class)
    public List<Restaurant> getRestaurantsByArea(@PathVariable String area) throws NotFoundException{
        logger.info("Getting Restaurants by area: " + area);
        return restaurantService.getRestaurantsByArea(area);
    }

    @GetMapping("/restaurants/getRestaurantsByCuisine/{cuisine}")
    @JsonView(View.RestaurantView.class)
    public List<Restaurant> getRestaurantsByCuisine(@PathVariable String cuisine) throws NotFoundException{
        logger.info("Getting Restaurants by cuisine: " + cuisine);
        return restaurantService.getRestaurantsByCuisine(cuisine);
    }

    @GetMapping("/restaurants/getAllCuisines")
    public List<String> getAllCuisines(){
        logger.info("Getting all cuisines");
        return restaurantService.getListOfCuisines();
    }

    @GetMapping("/restaurants/getAllAreas")
    public List<String> getAllAreas(){
        logger.info("Getting all areas");
        return Arrays.asList("NORTH", "NORTHEAST", "EAST", "WEST", "CENTRAL");
    }

    @GetMapping("/restaurants/getPriceRange")
    public List<String> getPriceRange(){
        logger.info("Getting all price range");
        return restaurantService.getPriceRanges();
    }

    @GetMapping("/restaurants/getRestaurantsByPriceRange/{priceRange}")
    @JsonView(View.RestaurantView.class)
    public List<Restaurant> getRestaurantsByPriceRange(@PathVariable String priceRange) throws NotFoundException{
        logger.info("Getting Restaurants by price range: " + priceRange);
        return restaurantService.getRestaurantsByPriceRange(priceRange);
    }

    @GetMapping("/restaurants/getAllRestaurantsByAdminId/{adminId}")
    public ResponseEntity<?> getAllRestaurantsByAdminId(@PathVariable String adminId){
        List<UpdatedRestaurant> toReturn = new ArrayList<>();
        List<Restaurant> restaurants = (List<Restaurant>) restaurantRepository.findAll();

        for(int i = 0; i < restaurants.size(); i++){
            Restaurant restaurant = restaurants.get(i);
            if(restaurant.getAdminId().equals(adminId)){
                UpdatedRestaurant updatedRestaurant = new UpdatedRestaurant();
                updatedRestaurant.setRestaurantId(restaurant.getRestaurantId());
                updatedRestaurant.setRestaurantName(restaurant.getRestaurantName());
                updatedRestaurant.setRestaurantDescription(restaurant.getRestaurantDescription());
                updatedRestaurant.setContactNumber(restaurant.getRestaurantContactNumber());
                updatedRestaurant.setBuilding(restaurant.getBuilding());
                updatedRestaurant.setStreet(restaurant.getStreet());
                updatedRestaurant.setPostalCode(restaurant.getPostalCode());
                updatedRestaurant.setCuisine(restaurant.getCuisine());
                updatedRestaurant.setOperatingHours(restaurant.getRestaurantOpeningHours());
                updatedRestaurant.setAffordability(restaurant.getRestaurantPriceRange());
                byte[] restaurantImgBytes = restaurant.getRestaurantImgPath();
                String restaurantImg = new String(restaurantImgBytes);
                updatedRestaurant.setRestaurantImg(restaurantImg);
                toReturn.add(updatedRestaurant);
            }
        }
        return new ResponseEntity(toReturn, HttpStatus.OK);
    }
}

@Data
class UpdatedRestaurant{
    private String restaurantId;
    private String restaurantName;
    private String restaurantDescription;
    private String contactNumber;
    private String building;
    private String street;
    private String postalCode;
    private String cuisine;
    private String operatingHours;
    private String affordability;
    private String restaurantImg;

}
