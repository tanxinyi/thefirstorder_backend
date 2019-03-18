package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class UpdatedSubCategory {

    @JsonView(View.MainView.class)
    private String subCategoryId;

    @JsonView(View.MainView.class)
    private String categoryId;

    @JsonView(View.MainView.class)
    private String subCategoryName;

    @JsonView(View.MainView.class)
    private String subCategoryImage;

    @JsonView(View.SubCategoryView.class)
    private FoodCategory foodCategory;

    @JsonView(View.SubCategoryView.class)
    private List<UpdatedFoodPrice> foodPrices;

    public UpdatedSubCategory(SubCategory subCategory){
        this.subCategoryId = subCategory.getSubCategoryId();
        this.categoryId = subCategory.getCategoryId();
        this.subCategoryName = subCategory.getSubCategoryName();
        byte[] img = subCategory.getSubCategoryImage();
        String image = "";
        if(img != null) image = new String(img);
        this.subCategoryImage = image;
        this.foodCategory = subCategory.getFoodCategory();
        List<UpdatedFoodPrice> newFoodPrices = new ArrayList<>();
        for(FoodPrice foodPrice : subCategory.getFoodPrices()){
            newFoodPrices.add(new UpdatedFoodPrice(foodPrice));
        }
        this.foodPrices = newFoodPrices;
    }
}
