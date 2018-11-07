package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.*;
import MakaNow.thefirstorder_back.repository.ActivityLogRepository;
import MakaNow.thefirstorder_back.repository.MenuRepository;
import MakaNow.thefirstorder_back.repository.RestaurantRepository;
import MakaNow.thefirstorder_back.service.ActivityLogService;
import MakaNow.thefirstorder_back.service.MenuService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class MenuController {

    Logger logger = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Autowired
    private MenuService menuService;

    @Autowired
    private ActivityLogService activityLogService;

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

    @GetMapping("/restaurants/{restaurantId}/full_menu")
    @JsonView(View.ViewA.class)
    public Menu getLatestFullMenuByRestaurant( @PathVariable String restaurantId) throws NotFoundException {
        return getLatestFullMenu(restaurantId);
    }

    @GetMapping("/restaurants/{restaurantId}/menu")
    @JsonView(View.MainView.class)
    public Menu getLatestMenuByRestaurant(@PathVariable String restaurantId) throws NotFoundException {
        logger.info("Getting Latest Menu by RestaurantID");
        return getLatestFullMenu(restaurantId);
    }

    private Menu getLatestFullMenu(String restaurantId) throws NotFoundException {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
        if(optionalRestaurant.isPresent()){
            List<Menu> menus = optionalRestaurant.get().getMenus();
            Collections.sort(menus);
            return menus.get(0);
        }else{
            throw new NotFoundException("Restaurant ID:" + restaurantId + " does not exist");
        }
    }

    @GetMapping("/menu/{menuId}/categories")
    @JsonView(View.MainView.class)
    public List<Category> getCategoriesByMenu(@PathVariable String menuId) throws NotFoundException{
        logger.info("Getting List of Categories by MenuID");

        Menu menu = getMenuById(menuId);
        List<FoodPrice> foodPrices = menu.getFoodPrices();
        List<Category> categories = new ArrayList<>();

        for(FoodPrice foodPrice: foodPrices){
            Category category = foodPrice.getFood().getCategory();
            if(!categories.contains(category)){
                categories.add(category);
            }
        }
        return categories;
    }

    @GetMapping("/menu/{menuId}/category/{categoryId}")
    @JsonView(View.ViewB.class)
    public List<FoodPrice> getFoodItemsByCategory(@PathVariable String menuId, @PathVariable String categoryId) throws NotFoundException{
        logger.info("Getting List of FoodPrices by MenuID and Category");

        Menu menu = getMenuById(menuId);
        List<FoodPrice> foodPrices = menu.getFoodPrices();
        List<FoodPrice> output = new ArrayList<>();

        for(FoodPrice foodPrice: foodPrices){
            if (foodPrice.getFood().getCategory().getCategoryId().equals(categoryId)) output.add(foodPrice);
        }
        if(output.size()==0) throw new NotFoundException("Category Not Found");
        return output;
    }

    @PostMapping("/restaurants/{restaurantId}/menus")
    @JsonView({View.ViewA.class})
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

    @GetMapping("/menus/getMenu/{menuId}")
    public Menu getMenuByMenuId( @PathVariable("menuId") String menuId ){
        return menuService.getMenuByMenuId(menuId);
    }

    @GetMapping("/menus/getMenusByRestaurantId/{restaurantId}")
    @JsonView(View.ViewB.class)
    public ResponseEntity<?> getMenusByRestaurantId( @PathVariable("restaurantId") String restaurantId ){
        List<Menu> result;
        result = menuService.getMenusByRestaurantId(restaurantId);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @PostMapping("/menus/addMenu/{managerId}/{restaurantId}")
    public ResponseEntity<?> addMenu(@PathVariable("managerId") String managerId, @PathVariable("restaurantId") String restaurantId) throws ParseException {
        String newMenuId = menuService.getNewMenuId();

        String dateNow = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = sdf1.parse(dateNow);
        java.sql.Date sqlDateNow = new java.sql.Date(date.getTime());

        Menu newMenu = new Menu();

        newMenu.setMenuId(newMenuId);
        newMenu.setRestaurantId(restaurantId);
        newMenu.setDateOfCreation(sqlDateNow);

        menuRepository.save(newMenu);

        ActivityLog activityLog = new ActivityLog();

        String newActivityLogId = activityLogService.getNewActivityLogId();
        activityLog.setActivityLogId(newActivityLogId);
        activityLog.setManagerId(managerId);
        activityLog.setRestaurantId(restaurantId);

        String description = "Added Menu '" + newMenuId + "' to Restaurant '" + restaurantId + "'";
        activityLog.setDescription(description);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        activityLog.setChangeTimeStamp(timestamp);

        activityLogRepository.save(activityLog);

        return new ResponseEntity("Menu Added Successfully", HttpStatus.OK);
    }

    @DeleteMapping("/menus")
    public ResponseEntity<?> deleteMenuByMenuId(@RequestParam("menuId") String menuId, @RequestParam("restaurantId") String restaurantId, @RequestParam("managerId") String managerId){
        menuRepository.deleteById(menuId);

        ActivityLog activityLog = new ActivityLog();

        String newActivityLogId = activityLogService.getNewActivityLogId();
        activityLog.setActivityLogId(newActivityLogId);
        activityLog.setManagerId(managerId);
        activityLog.setRestaurantId(restaurantId);

        String description = "Deleted Menu '" + menuId + "' from Restaurant '" + restaurantId + "'";
        activityLog.setDescription(description);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        activityLog.setChangeTimeStamp(timestamp);

        activityLogRepository.save(activityLog);

        return new ResponseEntity("Menu Deleted Successfully", HttpStatus.OK);
    }
}
