package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.*;
import MakaNow.thefirstorder_back.repository.FoodCategoryRepository;
import MakaNow.thefirstorder_back.repository.FoodRepository;
import MakaNow.thefirstorder_back.repository.SubCategoryRepository;
import MakaNow.thefirstorder_back.service.FoodService;
import com.fasterxml.jackson.annotation.JsonView;
import javassist.NotFoundException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class FoodController {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private FoodService foodService;

    @Autowired
    private FoodCategoryRepository foodCategoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

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
        List<FoodItemInMenu1> result1 = new ArrayList<>();
        List<List> toReturn = new ArrayList<>();
        List<Food> foods = foodService.getFoodItemsByMenuId(menuId);
//        Map<String, List<String>> foodCustomisationsWithOptions = new HashMap<>();

        for (int i = 0; i < foods.size(); i++){
            Food food = foods.get(i);
            List<FoodPrice> foodPrices = food.getFoodPrices();
            for (int j = 0; j < foodPrices.size(); j++){
                FoodPrice foodPrice = foodPrices.get(j);
                if (foodPrice.getMenuFoodCatId().getMenuId().equals(menuId)){
                    FoodItemInMenu foodItemInMenu = new FoodItemInMenu();
                    FoodItemInMenu1 foodItemInMenu1 = new FoodItemInMenu1();
                    foodItemInMenu.setFoodId(food.getFoodId());
                    foodItemInMenu.setFoodName(food.getFoodName());
                    foodItemInMenu1.setFoodName(food.getFoodName());
                    foodItemInMenu.setFoodDescription(food.getFoodDescription());
                    foodItemInMenu1.setFoodDescription(food.getFoodDescription());
                    byte[] foodImgBytes = food.getFoodImgPath();
                    String foodImg = new String(foodImgBytes);
//                    String foodImg = Base64.getEncoder().encodeToString(foodImgBytes);

                    foodItemInMenu.setFoodCategory(foodPrice.getFoodCategory().getFoodCategoryId());
                    foodItemInMenu.setFoodCategoryName(foodCategoryRepository.findById(foodPrice.getFoodCategory().getFoodCategoryId()).get().getFoodCategoryName());
                    foodItemInMenu1.setFoodCategoryName(foodCategoryRepository.findById(foodPrice.getFoodCategory().getFoodCategoryId()).get().getFoodCategoryName());
                    foodItemInMenu.setFoodSubCategory(foodPrice.getSubCategoryId());
                    if(foodPrice.getSubCategoryId() != null){
                        foodItemInMenu.setSubCategoryName(subCategoryRepository.findById(foodPrice.getSubCategoryId()).get().getSubCategoryName());
                        foodItemInMenu1.setSubCategoryName(subCategoryRepository.findById(foodPrice.getSubCategoryId()).get().getSubCategoryName());
                    }else{
                        foodItemInMenu.setSubCategoryName("No Sub Category");
                        foodItemInMenu1.setSubCategoryName("No Sub Category");
                    }
                    foodItemInMenu.setFoodPrice(foodPrice.getFoodPrice());
                    foodItemInMenu1.setFoodPrice(foodPrice.getFoodPrice());
                    foodItemInMenu.setFoodAvailability(foodPrice.isAvailability());
                    foodItemInMenu1.setFoodAvailability(foodPrice.isAvailability());
                    foodItemInMenu.setFoodImg(foodImg);

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
                    result1.add(foodItemInMenu1);
                }
            }
        }
        toReturn.add(result);
        toReturn.add(result1);
        return new ResponseEntity(toReturn, HttpStatus.OK);
    }

    @GetMapping("/foods/getFoodsByCategory/{menuId}/{categoryId}")
//    @JsonView(View.FoodView.class)
    public ResponseEntity<?> getFoodItemsByMenuIdAndCategoryId( @PathVariable("menuId") String menuId, @PathVariable("categoryId") String categoryId ){
        List<FoodItemInMenu> result = new ArrayList<>();
        List<FoodItemInMenu1> result1 = new ArrayList<>();
        List<List> toReturn = new ArrayList<>();
        List<Food> foods = foodService.getFoodItemsByMenuId(menuId);

        for (int i = 0; i < foods.size(); i++){
            Food food = foods.get(i);
            List<FoodPrice> foodPrices = food.getFoodPrices();
            for (int j = 0; j < foodPrices.size(); j++){
                FoodPrice foodPrice = foodPrices.get(j);
                if (foodPrice.getMenuFoodCatId().getMenuId().equals(menuId) && foodPrice.getFoodCategory().getFoodCategoryId().equals(categoryId)){
                    FoodItemInMenu foodItemInMenu = new FoodItemInMenu();
                    FoodItemInMenu1 foodItemInMenu1 = new FoodItemInMenu1();
                    foodItemInMenu.setFoodId(food.getFoodId());
                    foodItemInMenu.setFoodName(food.getFoodName());
                    foodItemInMenu1.setFoodName(food.getFoodName());
                    foodItemInMenu.setFoodDescription(food.getFoodDescription());
                    foodItemInMenu1.setFoodDescription(food.getFoodDescription());
                    byte[] foodImgBytes = food.getFoodImgPath();
                    String foodImg = new String(foodImgBytes);

                    foodItemInMenu.setFoodCategory(foodPrice.getFoodCategory().getFoodCategoryId());
                    foodItemInMenu.setFoodCategoryName(foodCategoryRepository.findById(foodPrice.getFoodCategory().getFoodCategoryId()).get().getFoodCategoryName());
                    foodItemInMenu1.setFoodCategoryName(foodCategoryRepository.findById(foodPrice.getFoodCategory().getFoodCategoryId()).get().getFoodCategoryName());
                    foodItemInMenu.setFoodSubCategory(foodPrice.getSubCategoryId());
                    if(foodPrice.getSubCategoryId() != null){
                        foodItemInMenu.setSubCategoryName(subCategoryRepository.findById(foodPrice.getSubCategoryId()).get().getSubCategoryName());
                        foodItemInMenu1.setSubCategoryName(subCategoryRepository.findById(foodPrice.getSubCategoryId()).get().getSubCategoryName());
                    }else{
                        foodItemInMenu.setSubCategoryName("No Sub Category");
                        foodItemInMenu1.setSubCategoryName("No Sub Category");
                    }
                    foodItemInMenu.setFoodPrice(foodPrice.getFoodPrice());
                    foodItemInMenu1.setFoodPrice(foodPrice.getFoodPrice());
                    foodItemInMenu.setFoodAvailability(foodPrice.isAvailability());
                    foodItemInMenu1.setFoodAvailability(foodPrice.isAvailability());
                    foodItemInMenu.setFoodImg(foodImg);

                    result.add(foodItemInMenu);
                    result1.add(foodItemInMenu1);
                }
            }
        }
        toReturn.add(result);
        toReturn.add(result1);
        return new ResponseEntity(toReturn, HttpStatus.OK);
    }

    @GetMapping("/foods/getFoodsBySubCategory/{menuId}/{subCategoryId}")
//    @JsonView(View.FoodView.class)
    public ResponseEntity<?> getFoodItemsByMenuIdAndSubCategoryId( @PathVariable("menuId") String menuId, @PathVariable("subCategoryId") String subCategoryId ){
        List<FoodItemInMenu> result = new ArrayList<>();
        List<FoodItemInMenu1> result1 = new ArrayList<>();
        List<List> toReturn = new ArrayList<>();
        List<Food> foods = foodService.getFoodItemsByMenuId(menuId);

        for (int i = 0; i < foods.size(); i++){
            Food food = foods.get(i);
            List<FoodPrice> foodPrices = food.getFoodPrices();
            for (int j = 0; j < foodPrices.size(); j++){
                FoodPrice foodPrice = foodPrices.get(j);
                if (foodPrice.getMenuFoodCatId().getMenuId().equals(menuId) && foodPrice.getSubFoodCategory() != null){
                    if(foodPrice.getSubFoodCategory().getSubCategoryId().equals(subCategoryId)){
                        FoodItemInMenu foodItemInMenu = new FoodItemInMenu();
                        FoodItemInMenu1 foodItemInMenu1 = new FoodItemInMenu1();
                        foodItemInMenu.setFoodId(food.getFoodId());
                        foodItemInMenu.setFoodName(food.getFoodName());
                        foodItemInMenu1.setFoodName(food.getFoodName());
                        foodItemInMenu.setFoodDescription(food.getFoodDescription());
                        foodItemInMenu1.setFoodDescription(food.getFoodDescription());
                        byte[] foodImgBytes = food.getFoodImgPath();
                        String foodImg = new String(foodImgBytes);

                        foodItemInMenu.setFoodCategory(foodPrice.getFoodCategory().getFoodCategoryId());
                        foodItemInMenu.setFoodCategoryName(foodCategoryRepository.findById(foodPrice.getFoodCategory().getFoodCategoryId()).get().getFoodCategoryName());
                        foodItemInMenu1.setFoodCategoryName(foodCategoryRepository.findById(foodPrice.getFoodCategory().getFoodCategoryId()).get().getFoodCategoryName());
                        foodItemInMenu.setFoodSubCategory(foodPrice.getSubCategoryId());
                        if(foodPrice.getSubCategoryId() != null){
                            foodItemInMenu.setSubCategoryName(subCategoryRepository.findById(foodPrice.getSubCategoryId()).get().getSubCategoryName());
                            foodItemInMenu1.setSubCategoryName(subCategoryRepository.findById(foodPrice.getSubCategoryId()).get().getSubCategoryName());
                        }else{
                            foodItemInMenu.setSubCategoryName("No Sub Category");
                            foodItemInMenu1.setSubCategoryName("No Sub Category");
                        }
                        foodItemInMenu.setFoodPrice(foodPrice.getFoodPrice());
                        foodItemInMenu1.setFoodPrice(foodPrice.getFoodPrice());
                        foodItemInMenu.setFoodAvailability(foodPrice.isAvailability());
                        foodItemInMenu1.setFoodAvailability(foodPrice.isAvailability());
                        foodItemInMenu.setFoodImg(foodImg);

                        result.add(foodItemInMenu);
                        result1.add(foodItemInMenu1);
                    }
                }
            }
        }
        toReturn.add(result);
        toReturn.add(result1);
        return new ResponseEntity(toReturn, HttpStatus.OK);
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
    private String foodCategoryName;
    private String foodSubCategory;
    private String subCategoryName;
    private double foodPrice;
    private boolean foodAvailability;
    private String foodImg;
}

@Data
class FoodItemInMenu1 {
    private String foodName;
    private String foodDescription;
    private String foodCategoryName;
    private String subCategoryName;
    private double foodPrice;
    private boolean foodAvailability;
}