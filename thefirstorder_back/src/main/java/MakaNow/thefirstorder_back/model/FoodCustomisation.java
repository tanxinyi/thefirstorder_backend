package MakaNow.thefirstorder_back.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class FoodCustomisation {
    @Id
    private String foodCustomisationId;


    @Embedded
    private MenuFoodId menuFoodId;

    private String description;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name="food_price_menu_id", referencedColumnName = "menu_id"),
            @JoinColumn(name="food_price_food_id", referencedColumnName = "food_id")
    } )
    private FoodPrice foodPrice;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "foodCustomisation")
    private List<CustomisationOption> customisationOptions;
}
