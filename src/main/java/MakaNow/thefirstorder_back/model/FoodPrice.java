package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name="food_price")
public class FoodPrice {

    @EmbeddedId
    @JsonView(View.MainView.class)
    private MenuFoodCatId menuFoodCatId;

    @JsonView(View.MainView.class)
    @Column(name="sub_category_id")
    private String subCategoryId;

    @JsonView(View.MainView.class)
    private double foodPrice;

    @Column(name="availability", nullable = false, columnDefinition = "TINYINT(1)")
    @JsonView(View.MainView.class)
    private boolean availability;

    @ManyToOne
    @JoinColumn(name="menu_id", insertable = false, updatable = false)
    @JsonView(View.FoodPriceView.class)
    private Menu menu;

    @ManyToOne
    @JoinColumn(name="food_id", insertable = false, updatable = false)
    @JsonView(View.FoodPriceView.class)
    private Food food;

    @ManyToOne
    @JoinColumn(name="food_category_id", insertable = false, updatable = false)
    @JsonView(View.FoodPriceView.class)
    private FoodCategory foodCategory;

    @ManyToOne
    @JoinColumn(name="sub_category_id", insertable = false, updatable = false)
    @JsonView(View.FoodPriceView.class)
    private SubCategory subFoodCategory;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL},mappedBy = "foodPrice")
    @JsonView(View.FoodPriceView.class)
    private List<Customisation> customisations;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "foodPrice")
    @JsonView(View.FoodPriceView.class)
    private List<CustomerOrder> customerOrders;
}
