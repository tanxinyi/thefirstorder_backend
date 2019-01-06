package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.*;
import MakaNow.thefirstorder_back.repository.FoodRepository;
import MakaNow.thefirstorder_back.service.FoodService;
import com.fasterxml.jackson.annotation.JsonView;
import javassist.NotFoundException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class FoodController {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private FoodService foodService;

    @Autowired
    private CustomisationController customisationController;

    @GetMapping("/foodItems")
    @JsonView(View.FoodView.class)
    public List<Food> getAllFoodItems(){
        return (List<Food>) foodRepository.findAll();
    }

    @GetMapping("/foodItems/{foodId}")
    @JsonView(View.FoodView.class)
    public Food getFoodById( @PathVariable String foodId ) throws NotFoundException {
        Optional<Food> optionalFood = foodRepository.findById(foodId);
        if(optionalFood.isPresent()){
            return optionalFood.get();
        }else{
            throw new NotFoundException("Menu ID:" + foodId + " does not exist");
        }
    }

    @PostMapping("/foods")
    @JsonView(View.FoodView.class)
    public List<Food> getAllFoods(){
        return (List<Food>) foodRepository.findAll();
    }

    @GetMapping("/foods/{menuId}")
    public ResponseEntity<?> getFoodItemsByMenuId( @PathVariable("menuId") String menuId ) throws NotFoundException {
        List<FoodItemInMenu> result = new ArrayList<>();
        List<Food> foods = foodService.getFoodItemsByMenuId(menuId);
//        Map<String, List<String>> foodCustomisationsWithOptions = new HashMap<>();

        for (int i = 0; i < foods.size(); i++){
            Food food = foods.get(i);
            List<FoodPrice> foodPrices = food.getFoodPrices();
            for (int j = 0; j < foodPrices.size(); j++){
                FoodPrice foodPrice = foodPrices.get(j);
                if (foodPrice.getMenuFoodCatId().getMenuId().equals(menuId)){
                    FoodItemInMenu foodItemInMenu = new FoodItemInMenu();
                    foodItemInMenu.setFoodId(food.getFoodId());
                    foodItemInMenu.setFoodName(food.getFoodName());
                    foodItemInMenu.setFoodDescription(food.getFoodDescription());
                    foodItemInMenu.setFoodCategory(foodPrice.getFoodCategory().getFoodCategoryId());
                    foodItemInMenu.setFoodSubCategory(foodPrice.getSubCategoryId());
                    foodItemInMenu.setFoodPrice(foodPrice.getFoodPrice());
                    foodItemInMenu.setFoodAvailability(foodPrice.isAvailability());

//                    List<Customisation> foodCustomisations = customisationController.getCustomisationByMenuFoodCatId(menuId, food.getFoodId(), foodPrice.getFoodCategory().getFoodCategoryId());
//                    for(int k = 0; k < foodCustomisations.size(); k++){
//                        Customisation foodCustomisation = foodCustomisations.get(k);
//                        List<String> customisationOptionNames = new ArrayList<>();
//                        List<CustomisationOption> customisationOptions = foodCustomisation.getCustomisationOptions();
//                        for(int l = 0; l < customisationOptions.size(); l++){
//                            CustomisationOption customisationOption = customisationOptions.get(l);
//                            customisationOptionNames.add(customisationOption.getOptionDescription());
//                        }
//                        foodCustomisationsWithOptions.put(foodCustomisation.getCustomisationName(), customisationOptionNames);
//                    }
//
//                    foodItemInMenu.setFoodCustomisations(foodCustomisationsWithOptions);

                    result.add(foodItemInMenu);
                }
            }
        }
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping("/foods/getFoodsByCategory/{menuId}/{categoryId}")
//    @JsonView(View.FoodView.class)
    public ResponseEntity<?> getFoodItemsByMenuIdAndCategoryId( @PathVariable("menuId") String menuId, @PathVariable("categoryId") String categoryId ){
        List<FoodItemInMenu> result = new ArrayList<>();
        List<Food> foods = foodService.getFoodItemsByMenuId(menuId);

        for (int i = 0; i < foods.size(); i++){
            Food food = foods.get(i);
            List<FoodPrice> foodPrices = food.getFoodPrices();
            for (int j = 0; j < foodPrices.size(); j++){
                FoodPrice foodPrice = foodPrices.get(j);
                if (foodPrice.getMenuFoodCatId().getMenuId().equals(menuId) && foodPrice.getFoodCategory().getFoodCategoryId().equals(categoryId)){
                    FoodItemInMenu foodItemInMenu = new FoodItemInMenu();
                    foodItemInMenu.setFoodId(food.getFoodId());
                    foodItemInMenu.setFoodName(food.getFoodName());
                    foodItemInMenu.setFoodDescription(food.getFoodDescription());
                    foodItemInMenu.setFoodCategory(foodPrice.getFoodCategory().getFoodCategoryId());
                    foodItemInMenu.setFoodSubCategory(foodPrice.getSubCategoryId());
                    foodItemInMenu.setFoodPrice(foodPrice.getFoodPrice());
                    foodItemInMenu.setFoodAvailability(foodPrice.isAvailability());
                    result.add(foodItemInMenu);
                }
            }
        }
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping("/foods/getFoodsBySubCategory/{menuId}/{subCategoryId}")
//    @JsonView(View.FoodView.class)
    public ResponseEntity<?> getFoodItemsByMenuIdAndSubCategoryId( @PathVariable("menuId") String menuId, @PathVariable("subCategoryId") String subCategoryId ){
        List<FoodItemInMenu> result = new ArrayList<>();
        List<Food> foods = foodService.getFoodItemsByMenuId(menuId);

        for (int i = 0; i < foods.size(); i++){
            Food food = foods.get(i);
            List<FoodPrice> foodPrices = food.getFoodPrices();
            for (int j = 0; j < foodPrices.size(); j++){
                FoodPrice foodPrice = foodPrices.get(j);
                if (foodPrice.getMenuFoodCatId().getMenuId().equals(menuId) && foodPrice.getSubFoodCategory() != null){
                    if(foodPrice.getSubFoodCategory().getSubCategoryId().equals(subCategoryId)){
                        FoodItemInMenu foodItemInMenu = new FoodItemInMenu();
                        foodItemInMenu.setFoodId(food.getFoodId());
                        foodItemInMenu.setFoodName(food.getFoodName());
                        foodItemInMenu.setFoodDescription(food.getFoodDescription());
                        foodItemInMenu.setFoodCategory(foodPrice.getFoodCategory().getFoodCategoryId());
                        foodItemInMenu.setFoodSubCategory(foodPrice.getSubCategoryId());
                        foodItemInMenu.setFoodPrice(foodPrice.getFoodPrice());
                        foodItemInMenu.setFoodAvailability(foodPrice.isAvailability());
                        result.add(foodItemInMenu);
                    }
                }
            }
        }
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @PostMapping("/foods/getNewFoodId")
    @JsonView(View.FoodView.class)
    public String getNewFoodId(){
        return foodService.getNewFoodId();
    }
}

@Data
class FoodItemInMenu{
    private String foodId;
    private String foodName;
    private String foodDescription;
    private String foodCategory;
    private String foodSubCategory;
    private double foodPrice;
    private boolean foodAvailability;
//    private Map<String, List<String>> foodCustomisations;

}
