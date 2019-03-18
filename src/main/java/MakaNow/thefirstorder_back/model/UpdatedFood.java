package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Data
public class UpdatedFood {
    @JsonView(View.MainView.class)
    private String foodId;

    @JsonView(View.MainView.class)
    private String foodName;

    @JsonView(View.MainView.class)
    private String foodDescription;

    @JsonView(View.MainView.class)
    private String foodImgPath;

    @JsonView(View.FoodView.class)
    private List<FoodPrice> foodPrices;

    public UpdatedFood(Food food){
        this.foodId = food.getFoodId();
        this.foodName = food.getFoodName();
        this.foodDescription = food.getFoodDescription();
        byte[] img = food.getFoodImgPath();
        String image = "";
        if(img != null) image = new String(img);
        this.foodImgPath = image;
        this.foodPrices = food.getFoodPrices();
    }
}
