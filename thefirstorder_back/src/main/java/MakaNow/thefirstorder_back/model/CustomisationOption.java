package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class CustomisationOption {
    @Id
    private String customisationOptionId;;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="food_customisation_id")
    private FoodCustomisation foodCustomisation;

    private MenuFoodId menuFoodId;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    @JoinColumns({
            @JoinColumn(name="menu_id", referencedColumnName = "menu_id", insertable = false, updatable = false),
            @JoinColumn(name="food_id", referencedColumnName = "food_id", insertable = false, updatable = false)
    } )
    private FoodPrice foodPrice;





    private String description;
    private double price;

}
