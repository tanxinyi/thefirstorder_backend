package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Table(name="food_price")
public class UpdatedFoodPrice {

    @JsonView(View.MainView.class)
    private MenuFoodCatId menuFoodCatId;

    @JsonView(View.MainView.class)
    @Column(name="sub_category_id")
    private String subCategoryId;

    @JsonView(View.MainView.class)
    private double foodPrice;

    @JsonView(View.MainView.class)
    private boolean availability;

    @JsonView(View.FoodPriceView.class)
    private Menu menu;

    @JsonView(View.FoodPriceView.class)
    private UpdatedFood food;

    @JsonView(View.FoodPriceView.class)
    private FoodCategory foodCategory;

    @JsonView(View.FoodPriceView.class)
    private SubCategory subFoodCategory;

    @JsonView(View.FoodPriceView.class)
    private List<Customisation> customisations;

    @JsonView(View.FoodPriceView.class)
    private List<CustomerOrder> customerOrders;

    public UpdatedFoodPrice(FoodPrice foodPrice){
        this.menuFoodCatId = foodPrice.getMenuFoodCatId();
        this.menu = foodPrice.getMenu();
        this.availability = foodPrice.isAvailability();
        this.foodPrice = foodPrice.getFoodPrice();
        this.subCategoryId = foodPrice.getSubCategoryId();
        this.food = new UpdatedFood(foodPrice.getFood());
        this.foodCategory = foodPrice.getFoodCategory();
        this.subFoodCategory = foodPrice.getSubFoodCategory();
        this.customisations = foodPrice.getCustomisations();
        this.customerOrders = foodPrice.getCustomerOrders();
    }
}
