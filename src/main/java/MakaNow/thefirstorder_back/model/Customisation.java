package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Customisation {
    @Id
    @JsonView(View.MainView.class)
    @Column(name="customisation_id")
    private String customisationId;

    @JsonView(View.MainView.class)
    private MenuFoodCatId menuFoodCatId;

    @JsonView(View.MainView.class)
    private String customisationName;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name="food_id", referencedColumnName = "food_id", insertable = false, updatable = false),
            @JoinColumn(name="menu_id", referencedColumnName = "menu_id", insertable = false, updatable = false),
            @JoinColumn(name="food_category_id", referencedColumnName = "food_category_id", insertable = false, updatable = false)
    })
    @JsonView(View.CustomisationView.class)
    private FoodPrice foodPrice;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy="customisation")
    @JsonView(View.CustomisationView.class)
    private List<CustomisationOption> customisationOptions;
}
