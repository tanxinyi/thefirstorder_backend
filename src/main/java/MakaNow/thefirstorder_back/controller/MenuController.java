package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.*;
import MakaNow.thefirstorder_back.repository.*;
import MakaNow.thefirstorder_back.service.*;
import com.fasterxml.jackson.annotation.JsonView;
import javassist.NotFoundException;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class MenuController {

    Logger logger = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private FoodPriceRepository foodPriceRepository;

    @Autowired
    private FoodCategoryRepository foodCategoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Autowired
    private MenuService menuService;

    @Autowired
    private FoodService foodService;

    @Autowired
    private ActivityLogService activityLogService;

    @Autowired
    private FoodCategoryService foodCategoryService;

    @Autowired
    private SubCategoryService subCategoryService;

    @GetMapping("/menus")
    @JsonView(View.MenuView.class)
    public List<Menu> getAllMenus(){
        return (List<Menu>) menuRepository.findAll();
    }

    @GetMapping("/menus/{menuId}")
    @JsonView(View.MenuView.class)
    public Menu getMenuById( @PathVariable String menuId ) throws NotFoundException {
        Optional<Menu> optionalMenu = menuRepository.findById(menuId);
        if(optionalMenu.isPresent()){
            return optionalMenu.get();
        }else{
            throw new NotFoundException("Menu ID:" + menuId + " does not exist");
        }
    }

    @GetMapping("/restaurants/{restaurantId}/full_menu")
    @JsonView(View.MenuView.class)
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
    @JsonView(View.CategoryView.class)
    public List<FoodCategory> getCategoriesByMenu(@PathVariable String menuId) throws NotFoundException{
        logger.info("Getting List of Categories by MenuID");

        Menu menu = getMenuById(menuId);
        List<FoodPrice> foodPrices = menu.getFoodPrices();
        List<FoodCategory> categories = new ArrayList<>();

        for(FoodPrice foodPrice: foodPrices){
            FoodCategory category = foodPrice.getFoodCategory();
            if(!categories.contains(category)){
                categories.add(category);
            }
        }
        return categories;
    }


    @GetMapping("/foodPrices/menu/{menuId}/category/{categoryId}")
    @JsonView(View.FoodPriceView.class)
    public List<FoodPrice> getFoodPriceByCategory(@PathVariable String menuId, @PathVariable String categoryId) throws NotFoundException{
        logger.info("Getting List of FoodPrices by MenuID and Category");

        Menu menu = getMenuById(menuId);
        List<FoodPrice> foodPrices = menu.getFoodPrices();
        List<FoodPrice> output = new ArrayList<>();

        for(FoodPrice foodPrice: foodPrices){
            if(foodPrice.getSubFoodCategory() == null){

                if (foodPrice.getFoodCategory().getFoodCategoryId().equals(categoryId)){

                    output.add(foodPrice);
                }
            }else if(foodPrice.getSubFoodCategory().getSubCategoryId().equals(categoryId)){
                output.add(foodPrice);
            }
        }


        if(output.size()==0) throw new NotFoundException("Category Not Found");

        return output;
    }

    @GetMapping("/subCategories/menu/{menuId}/category/{categoryId}")
    @JsonView(View.SubCategoryView.class)
    public List<SubCategory> getSubCategoriesByCategory(@PathVariable String menuId, @PathVariable String categoryId) throws NotFoundException{
        logger.info("Getting List of SubCategories by MenuID and Category");

        Menu menu = getMenuById(menuId);
        List<FoodPrice> foodPrices = menu.getFoodPrices();
        List<SubCategory> output = new ArrayList<>();
        boolean exist = false;

        for(FoodPrice foodPrice: foodPrices){
            if (foodPrice.getFoodCategory().getFoodCategoryId().equals(categoryId)){
                exist = true;
                SubCategory subCategory = foodPrice.getSubFoodCategory();
                if(subCategory != null && !output.contains(subCategory)){
                    output.add(subCategory);
                }
            }
        }

        if(!exist) throw new NotFoundException("Category Not Found");
        return output;
    }


    @PostMapping("/restaurants/{restaurantId}/menus")
    @JsonView({View.MenuView.class})
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
                    menu.setMenuCreationDate(menuUpdated.getMenuCreationDate());
                    return menuRepository.save(menu);
                }).orElseThrow(()-> new NotFoundException("Update Unsuccessful."));
    }

    @GetMapping("/menus/getMenu/{menuId}")
    public Menu getMenuByMenuId( @PathVariable("menuId") String menuId ){
        return menuService.getMenuByMenuId(menuId);
    }

    @GetMapping("/menus/getMenusByRestaurantId/{restaurantId}")
    @JsonView(View.MenuView.class)
    public ResponseEntity<?> getMenusByRestaurantId( @PathVariable("restaurantId") String restaurantId ){
        List<Menu> result;
        result = menuService.getMenusByRestaurantId(restaurantId);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @PostMapping("/menus/addMenu/{managerId}/{restaurantId}/{newMenuName}")
    public ResponseEntity<?> addMenu(@PathVariable("managerId") String managerId, @PathVariable("restaurantId") String restaurantId, @PathVariable("newMenuName") String newMenuName) throws ParseException {
        String newMenuId = menuService.getNewMenuId();

        String dateNow = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = sdf1.parse(dateNow);
        java.sql.Date sqlDateNow = new java.sql.Date(date.getTime());

        Menu newMenu = new Menu();

        newMenu.setMenuId(newMenuId);
        newMenu.setRestaurantId(restaurantId);
        newMenu.setMenuName(newMenuName);
        newMenu.setMenuCreationDate(sqlDateNow);

        menuRepository.save(newMenu);

        ActivityLog activityLog = new ActivityLog();

        String newActivityLogId = activityLogService.getNewActivityLogId();
        activityLog.setActivityLogId(newActivityLogId);
        activityLog.setManagerId(managerId);
        activityLog.setRestaurantId(restaurantId);

        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
        Restaurant restaurant1 = optionalRestaurant.get();

        String description = "Added '" + newMenuName + "' menu to '" + restaurant1.getRestaurantName() + "'";
        activityLog.setDescription(description);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        activityLog.setChangeTimeStamp(timestamp);

        activityLogRepository.save(activityLog);

        return new ResponseEntity("Menu Added Successfully", HttpStatus.OK);
    }

    @DeleteMapping("/menus")
    public ResponseEntity<?> deleteMenuByMenuId(@RequestParam("menuId") String menuId, @RequestParam("restaurantId") String restaurantId, @RequestParam("managerId") String managerId){
        List<FoodPrice> foodPrices = (List<FoodPrice>) foodPriceRepository.findAll();
////
//        for(int i = 0; i<foodPrices.size(); i++){
//            FoodPrice foodPrice = foodPrices.get(i);
//            MenuFoodCatId foodPricePK = foodPrice.getMenuFoodCatId();
//            if(foodPrice.getMenuFoodCatId().getMenuId().equals(menuId)){
//                List<Customisation> customisations = foodPrice.getCustomisations();
//                for(int k = 0; k < customisations.size(); k++){
//                    Customisation customisation = customisations.get(k);
//                    String customisationId = customisation.getCustomisationId();
//                    List<CustomisationOption> customisationOptions = customisation.getCustomisationOptions();
//                    for(int j = 0; j < customisationOptions.size(); j++){
//                        CustomisationOption customisationOption = customisationOptions.get(j);
//                        String customisationOptionId = customisationOption.getCustomisationOptionId();
//                        customisationOptionRepository.deleteById(customisationOptionId);
//                    }
//                    customisationRepository.deleteById(customisationId);
//                }
//                foodPriceRepository.deleteById(foodPricePK);
//            }
//        }

//        for(int i = 0; i < foodPrices.size(); i++){
//            FoodPrice foodPrice = foodPrices.get(i);
//            if(foodPrice.getMenuFoodCatId().getMenuId().equals(menuId)){
//                String foodCategoryId = foodPrice.getMenuFoodCatId().getFoodCategoryId();
//                foodCategoryRepository.deleteById(foodCategoryId);
//            }
//        }

        Menu menu = menuRepository.findById(menuId).get();
        String menuName = menu.getMenuName();

        menuRepository.deleteById(menuId);

        ActivityLog activityLog = new ActivityLog();

        String newActivityLogId = activityLogService.getNewActivityLogId();
        activityLog.setActivityLogId(newActivityLogId);
        activityLog.setManagerId(managerId);
        activityLog.setRestaurantId(restaurantId);

        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
        Restaurant restaurant1 = optionalRestaurant.get();

        String description = "Deleted '" + menuName + "' menu from '" + restaurant1.getRestaurantName() + "'";
        activityLog.setDescription(description);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        activityLog.setChangeTimeStamp(timestamp);

        activityLogRepository.save(activityLog);

        return new ResponseEntity("Menu Deleted Successfully", HttpStatus.OK);
    }

    @PostMapping("/menus/changeMenuName/{managerId}/{restaurantId}/{menuId}/{newMenuName}")
    public ResponseEntity<?> changeMenuName(@PathVariable("managerId") String managerId, @PathVariable("restaurantId") String restaurantId, @PathVariable("menuId") String menuId, @PathVariable("newMenuName") String newMenuName) throws ParseException {
        String oldMenuName = menuRepository.findById(menuId).get().getMenuName();
        menuRepository.findById(menuId).get().setMenuName(newMenuName);

        ActivityLog activityLog = new ActivityLog();

        String newActivityLogId = activityLogService.getNewActivityLogId();
        activityLog.setActivityLogId(newActivityLogId);
        activityLog.setManagerId(managerId);
        activityLog.setRestaurantId(restaurantId);

        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
        Restaurant restaurant1 = optionalRestaurant.get();

        String description = "Changed menu name '" + oldMenuName + "' to '" + newMenuName + " of '" + restaurant1.getRestaurantName() + "'";
        activityLog.setDescription(description);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        activityLog.setChangeTimeStamp(timestamp);

        activityLogRepository.save(activityLog);

        return new ResponseEntity("Menu Name Changed to '" + newMenuName + "'", HttpStatus.OK);
    }

    @RequestMapping(value = "/importCSV/{menuId}/{restaurantId}/{managerId}", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> importCSV(@RequestBody @Valid List<Map<String, String>> body, @PathVariable("menuId") String menuId, @PathVariable("restaurantId") String restaurantId, @PathVariable("managerId") String managerId) {
        try{
            for(int i = 0; i < body.size(); i++){
                Food newFood = new Food();
                String foodId = foodService.getNewFoodId();
                newFood.setFoodId(foodId);
                newFood.setFoodImgPath(new byte[0]);
                FoodPrice newFoodPrice = new FoodPrice();
                String categoryId1 = "";

                Map<String, String> map = body.get(i);
                List<String> foodItemDetails = new ArrayList<>(map.values());
                for(int k = 0; k < foodItemDetails.size(); k ++){
                    if(k==0){ //Food Name
                        String foodName = foodItemDetails.get(0).substring(1, foodItemDetails.get(0).length()-1);
                        newFood.setFoodName(foodName);
                    }else if (k==1){ //Food Description
                        String foodDesc = foodItemDetails.get(1).substring(1, foodItemDetails.get(1).length()-1);
                        newFood.setFoodDescription(foodDesc);
                    }else if (k==2){ //Food Category
                        String foodCategory1 = foodItemDetails.get(2).substring(1, foodItemDetails.get(2).length()-1);
                        boolean categoryAssigned = false;
                        List<FoodCategory> foodCategories = (List<FoodCategory>) foodCategoryRepository.findAll();
                        for(int j = 0; j < foodCategories.size(); j++){
                            FoodCategory foodCategory = foodCategories.get(j);
                            if(foodCategory.getFoodCategoryName().equals(foodCategory1)){
                                MenuFoodCatId newFoodPricePK = new MenuFoodCatId(menuId, foodId, foodCategory.getFoodCategoryId());
                                newFoodPrice.setMenuFoodCatId(newFoodPricePK);
                                categoryId1 = foodCategory.getFoodCategoryId();
                                categoryAssigned = true;
                                break;
                            }
                        }
                        if(!categoryAssigned) {
                            String newFoodCategoryId = foodCategoryService.getNewFoodCategoryId();
                            categoryId1 = newFoodCategoryId;
                            FoodCategory newFoodCategory = new FoodCategory();
                            newFoodCategory.setFoodCategoryId(newFoodCategoryId);
                            newFoodCategory.setFoodCategoryName(foodCategory1);
                            newFoodCategory.setFoodCategoryImgPath(new byte[0]);
                            foodCategoryRepository.save(newFoodCategory);
                            MenuFoodCatId newFoodPricePK = new MenuFoodCatId(menuId, foodId, newFoodCategoryId);
                            newFoodPrice.setMenuFoodCatId(newFoodPricePK);
                        }
                    }else if(k==3){ //Food Sub Category
                        String subCategory1 = foodItemDetails.get(3).substring(1, foodItemDetails.get(3).length()-1);
                        if(subCategory1.equals("No Sub Category")){
                            newFoodPrice.setSubCategoryId(null);
                        }else{
                            boolean subCategoryAssigned = false;
                            List<SubCategory> subCategories = (List<SubCategory>) subCategoryRepository.findAll();
                            for(int m = 0; m < subCategories.size(); m++){
                                SubCategory subCategory = subCategories.get(m);
                                if(subCategory.getSubCategoryName().equals(subCategory1)){
                                    newFoodPrice.setSubCategoryId(subCategory.getSubCategoryId());
                                    subCategoryAssigned = true;
                                    break;
                                }
                            }
                            if(!subCategoryAssigned){
                                String subCategoryId = subCategoryService.getNewSubCategoryId();
                                SubCategory subCategory = new SubCategory();
                                subCategory.setSubCategoryId(subCategoryId);
                                subCategory.setCategoryId(categoryId1);
                                subCategory.setSubCategoryName(subCategory1);
                                subCategory.setSubCategoryImage(new byte[0]);
                                subCategoryRepository.save(subCategory);
                            }
                        }
                    }else if(k==4){ //Food Price
                        String foodPrice = foodItemDetails.get(4).substring(1, foodItemDetails.get(4).length()-1);
                        newFoodPrice.setFoodPrice(Double.parseDouble(foodPrice));
                    }else if(k==5){ //Food Availablity
                        String foodAvailability = foodItemDetails.get(5).substring(1, foodItemDetails.get(5).length()-1);
                        if(foodAvailability.equals("TRUE")){
                            newFoodPrice.setAvailability(true);
                        }else{
                            newFoodPrice.setAvailability(false);
                        }
                    }
                }
                foodRepository.save(newFood);
                foodPriceRepository.save(newFoodPrice);
            }

            ActivityLog activityLog = new ActivityLog();

            String newActivityLogId = activityLogService.getNewActivityLogId();
            activityLog.setActivityLogId(newActivityLogId);
            activityLog.setManagerId(managerId);
            activityLog.setRestaurantId(restaurantId);

            Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
            Restaurant restaurant1 = optionalRestaurant.get();

            String description = "Imported new menu to '" + restaurant1.getRestaurantName() + "'";
            activityLog.setDescription(description);

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            activityLog.setChangeTimeStamp(timestamp);

            activityLogRepository.save(activityLog);

            return new ResponseEntity("Import Successful", HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity("Import Error! Ensure correct template is used.", HttpStatus.OK);
        }
    }
}
