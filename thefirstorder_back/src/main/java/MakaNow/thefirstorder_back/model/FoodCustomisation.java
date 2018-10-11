package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class FoodCustomisation {

    @Id
    @JsonView(View.MainView.class)
    private String foodCustomisationId;

    @Embedded
    private MenuFoodId menuFoodId;

    @JsonView(View.MainView.class)
    private String description;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns({
            @JoinColumn(name="menu_id", referencedColumnName = "menu_id", insertable = false, updatable = false),
            @JoinColumn(name="food_id", referencedColumnName = "food_id", insertable = false, updatable = false)
    } )
    private FoodPrice foodPrice;


    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "foodCustomisation")
    @JsonView(View.MainView.class)
    private List<CustomisationOption> customisationOptions;

}
