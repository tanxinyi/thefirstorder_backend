package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Data
public class CustomisationOption {
    @EmbeddedId
    private CustomisationOptionId customisationOptionId;

    private MenuFoodId menuFoodId;

    /*
    @ManyToOne
    @JsonIgnore
    private FoodPrice foodPrice;
    */

    @ManyToOne
    @JsonIgnore
    private FoodCustomisation foodCustomisation;

    private String description;
    private double price;

}
