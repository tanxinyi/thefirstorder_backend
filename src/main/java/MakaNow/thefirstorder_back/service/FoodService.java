package MakaNow.thefirstorder_back.service;

import MakaNow.thefirstorder_back.model.Food;
import MakaNow.thefirstorder_back.model.FoodPrice;
import MakaNow.thefirstorder_back.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;

    public List<Food> getFoodItemsByMenuId( String menuId){
        List<Food> results = new ArrayList<>();
        List<Food> foodItems = (List<Food>) foodRepository.findAll();
        List<FoodPrice> foodPrices;
        for (int i = 0; i < foodItems.size(); i++){
            Food food = foodItems.get(i);
            foodPrices = food.getFoodPrices();
            for(int j = 0; j < foodPrices.size(); j++){
                FoodPrice foodPrice = foodPrices.get(j);
                if (foodPrice.getMenuFoodCatId().getMenuId().equals(menuId)){
                    results.add(food);
                }
            }
        }
        return results;
    }

    public Food getFoodByFoodId(String foodId){
        Optional<Food> optionalFood = foodRepository.findById(foodId);
        if(optionalFood.isPresent()){
            Food food = optionalFood.get();
            return food;
        }else{
            return null;
        }
    }

    public String getNewFoodId(){
        List<String> foodIds = new ArrayList<>();
        List<Food> foods = (List<Food>) foodRepository.findAll();

        //For first food created to avoid index out of bounds error
        if(foods.size() == 0){
            return "F001";
        }

        for(int i = 0; i < foods.size(); i++){
            Food food = foods.get(i);
            String foodId = food.getFoodId();
            foodIds.add(foodId);
        }
        Collections.sort(foodIds);
        String lastFoodId = foodIds.get(foodIds.size()-1);
        int foodIdNumber = Integer.parseInt(lastFoodId.substring(2));
        int newFoodIdNumber = foodIdNumber + 1;
        int length = String.valueOf(newFoodIdNumber).length();

        if(length == 1) {
            return ("F00" + newFoodIdNumber);
        }
        else if(length == 2){
            return ("F0" + newFoodIdNumber);
        }
        return ("F" + newFoodIdNumber);
    }
}
