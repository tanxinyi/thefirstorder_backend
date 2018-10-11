package MakaNow.thefirstorder_back.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class MenuFoodId implements Serializable {
    @Column(name = "menu_id")
    private String menuId;
    @Column(name = "food_id")
    private String foodId;

}
