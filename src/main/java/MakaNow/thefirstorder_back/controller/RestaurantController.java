package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.Restaurant;
import MakaNow.thefirstorder_back.model.UpdatedRestaurant;
import MakaNow.thefirstorder_back.model.SeatingTable;
import MakaNow.thefirstorder_back.model.View;
import MakaNow.thefirstorder_back.repository.RestaurantRepository;
import MakaNow.thefirstorder_back.repository.SeatingTableRepository;
import MakaNow.thefirstorder_back.service.RestaurantService;
import MakaNow.thefirstorder_back.service.SeatingTableService;
import com.fasterxml.jackson.annotation.JsonView;
import javassist.NotFoundException;
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
    private SeatingTableRepository seatingTableRepository;

    @Autowired
    private SeatingTableService seatingTableService;

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

    @GetMapping("/updated_restaurants/{restaurantId}")
    @JsonView(View.RestaurantView.class)
    public UpdatedRestaurant getUpdatedRestaurantById( @PathVariable String restaurantId ) throws NotFoundException {
        return new UpdatedRestaurant(getRestaurantById(restaurantId));
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
        return new ResponseEntity("Restaurant updated successfully", HttpStatus.OK);
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

        for(int j = 0; j < 3; j++){
            String newTableId = seatingTableService.getNewTableId();
            SeatingTable seatingTable = new SeatingTable();
            seatingTable.setQrCode(newTableId);
            seatingTable.setRestaurantId(restaurantId);
            seatingTable.setTableCapacity(4);
            seatingTableRepository.save(seatingTable);
        }

        return new ResponseEntity("Restaurant added successfully", HttpStatus.OK);
    }

    @GetMapping("/restaurants/getRestaurantsByArea/{area}")
    @JsonView(View.RestaurantView.class)
    public List<Restaurant> getRestaurantsByArea(@PathVariable String area){
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

    @PostMapping("/restaurants/searchByName")
    @JsonView(View.RestaurantView.class)
    public List<Restaurant> searchRestaurantsByName(@RequestBody String name) throws NotFoundException{
        logger.info("Getting Restaurants by name: " + name);
        return restaurantService.getRestaurantsByName(name);
    }

    @PostMapping("/restaurants/search")
    @JsonView(View.RestaurantView.class)
    public List<Restaurant> searchRestaurants(@RequestBody String query) throws NotFoundException{
        query = query.replace("+", " ").substring(0,query.length()-1);
        logger.info("Searching Restaurants by query: " + query);
        return restaurantService.queryRestaurant(query);
    }

    @GetMapping("/restaurants/index/{index}")
    @JsonView(View.RestaurantView.class)
    public List<Restaurant> getTenRestaurants(@PathVariable int index){
        logger.info("Getting 10 Restaurants by index: " + index);
        List<Restaurant> restaurants = (List<Restaurant>)restaurantRepository.findAll();
        if(index > restaurants.size()) return new ArrayList<>();
        return restaurants.subList(index, Math.min(index + 10, restaurants.size()));
    }

    @GetMapping("/restaurants/getAllRestaurantsByAdminId/{adminId}")
    public ResponseEntity<?> getAllRestaurantsByAdminId(@PathVariable String adminId){
        List<UpdatedRestaurant> toReturn = new ArrayList<>();
        List<Restaurant> restaurants = (List<Restaurant>) restaurantRepository.findAll();

        for(int i = 0; i < restaurants.size(); i++){
            Restaurant restaurant = restaurants.get(i);
            if(restaurant.getAdminId().equals(adminId)){
                UpdatedRestaurant updatedRestaurant = new UpdatedRestaurant(restaurant);
                toReturn.add(updatedRestaurant);
            }
        }
        return new ResponseEntity(toReturn, HttpStatus.OK);
    }
}
