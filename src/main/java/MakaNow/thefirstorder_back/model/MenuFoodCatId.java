package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class MenuFoodCatId implements Serializable {

    @Column(name = "menu_id", nullable = false)
    @JsonView(View.MainView.class)
    private String menuId;

    @Column(name = "food_id", nullable = false)
    @JsonView(View.MainView.class)
    private String foodId;

    @Column(name = "food_category_id", nullable = false)
    @JsonView(View.MainView.class)
    private String foodCategoryId;

    public MenuFoodCatId(String menuId, String foodId, String foodCategoryId){
        this.menuId = menuId;
        this.foodId = foodId;
        this.foodCategoryId = foodCategoryId;
    }
}
