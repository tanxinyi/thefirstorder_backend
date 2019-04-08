package MakaNow.thefirstorder_back.service;

import MakaNow.thefirstorder_back.model.Food;
import MakaNow.thefirstorder_back.model.FoodCategory;
import MakaNow.thefirstorder_back.model.FoodPrice;
import MakaNow.thefirstorder_back.model.UpdatedCategory;
import MakaNow.thefirstorder_back.repository.FoodCategoryRepository;
import MakaNow.thefirstorder_back.repository.FoodPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FoodCategoryService {

    @Autowired
    private FoodCategoryRepository foodCategoryRepository;

    @Autowired
    private FoodPriceRepository foodPriceRepository;

    public List<FoodCategory> getCategoriesByMenuId(String menuId){
        Set<FoodCategory> results = new HashSet<FoodCategory>();

        List<FoodPrice> foodPrices = (List<FoodPrice>) foodPriceRepository.findAll();

        Set<String> categoryIdSet = new HashSet<String>();

        for(int i = 0; i < foodPrices.size(); i++){
            FoodPrice foodPrice = foodPrices.get(i);
            String foodPriceMenuId = foodPrice.getMenuFoodCatId().getMenuId();
            if(foodPriceMenuId.equals(menuId)){
                String categoryId = foodPrice.getMenuFoodCatId().getFoodCategoryId();
                categoryIdSet.add(categoryId);
            }
        }

        List<FoodCategory> categories = (List<FoodCategory>) foodCategoryRepository.findAll();

        for(int i = 0; i < categories.size(); i++){
            FoodCategory foodCategory = categories.get(i);
            if(categoryIdSet.contains(foodCategory.getFoodCategoryId())){
                results.add(foodCategory);
            }
        }
        return new ArrayList<FoodCategory>(results);
    }

    public String getNewFoodCategoryId(){
        List<String> foodCategoryIds = new ArrayList<>();
        List<FoodCategory> foodCategories = (List<FoodCategory>) foodCategoryRepository.findAll();

        //For first food created to avoid index out of bounds error
        if(foodCategories.size() == 0){
            return "FC001";
        }

        for(int i = 0; i < foodCategories.size(); i++){
            FoodCategory foodCategory = foodCategories.get(i);
            String foodId = foodCategory.getFoodCategoryId();
            foodCategoryIds.add(foodId);
        }
        Collections.sort(foodCategoryIds);
        String lastFoodCategoryId = foodCategoryIds.get(foodCategoryIds.size()-1);
        int foodCategoryIdNumber = Integer.parseInt(lastFoodCategoryId.substring(2));
        int newFoodCategoryIdNumber = foodCategoryIdNumber + 1;
        int length = String.valueOf(newFoodCategoryIdNumber).length();

        if(length == 1) {
            return ("FC00" + newFoodCategoryIdNumber);
        }
        else if(length == 2){
            return ("FC0" + newFoodCategoryIdNumber);
        }
        return ("FC" + newFoodCategoryIdNumber);
    }

    public List<UpdatedCategory> convertCategories(List<FoodCategory> foodCategories){
        List<UpdatedCategory> output = new ArrayList<>();
        for(FoodCategory foodCategory: foodCategories){
            output.add(new UpdatedCategory(foodCategory));
        }
        return output;
    }
}
