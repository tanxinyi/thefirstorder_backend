package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class UpdatedCategory {

    @JsonView(View.MainView.class)
    private String foodCategoryId;

    @JsonView(View.MainView.class)
    private String foodCategoryName;

    @Lob
    @JsonView(View.MainView.class)
    private String foodCategoryImgPath;

    @JsonView(View.CategoryView.class)
    private List<UpdatedFoodPrice> foodPrices;

    @JsonView(View.CategoryView.class)
    private List<UpdatedSubCategory> subCategories;

    public UpdatedCategory(FoodCategory foodCategory){
        this.foodCategoryId = foodCategory.getFoodCategoryId();
        this.foodCategoryName = foodCategory.getFoodCategoryName();
        byte[] img = foodCategory.getFoodCategoryImgPath();
        String image = "";
        if(img != null) image = new String(img);
        this.foodCategoryImgPath = image;
        List<UpdatedFoodPrice> newFoodPrices = new ArrayList<>();
        for(FoodPrice foodPrice: foodCategory.getFoodPrices()){
            newFoodPrices.add(new UpdatedFoodPrice(foodPrice));
        }
        this.foodPrices = newFoodPrices;
        List<UpdatedSubCategory> newSubCategory = new ArrayList<>();
        for(SubCategory subCategory: foodCategory.getSubCategories()){
            newSubCategory.add(new UpdatedSubCategory(subCategory));
        }
        this.subCategories = newSubCategory;
    }
}