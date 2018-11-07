package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class MenuFoodId implements Serializable {

    @Column(name = "menu_id", nullable = false)
    @JsonView(View.MainView.class)
    private String menuId;
    @Column(name = "food_id", nullable = false)
    @JsonView(View.MainView.class)
    private String foodId;

    public MenuFoodId(String menuId, String foodId){
        this.menuId = menuId;
        this.foodId = foodId;
    }
}
