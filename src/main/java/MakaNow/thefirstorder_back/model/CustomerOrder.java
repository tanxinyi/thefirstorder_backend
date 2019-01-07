package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class CustomerOrder {
    @Id
    @JsonView(View.MainView.class)
    private String customerOrderId;

    @JsonView(View.MainView.class)
    private MenuFoodCatId menuFoodCatId;

    @JsonView(View.MainView.class)
    @Column(name="order_id")
    private String orderId;

    @JsonView(View.MainView.class)
    private int customerOrderQuantity;

    @JsonView(View.MainView.class)
    private double customerOrderPrice;

    @JsonView(View.MainView.class)
    private String customerOrderRemarks;

    @ManyToOne
    @JoinColumn(name="order_id", insertable = false, updatable = false)
    @JsonIgnore
    private Orders order;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name="food_id", referencedColumnName = "food_id", insertable = false, updatable = false),
            @JoinColumn(name="menu_id", referencedColumnName = "menu_id", insertable = false, updatable = false),
            @JoinColumn(name="food_category_id", referencedColumnName = "food_category_id", insertable = false, updatable = false)
    })
    @JsonIgnore
    private FoodPrice foodPrice;

    @ManyToMany (cascade = CascadeType.ALL)
    @JoinTable(
            name="customisation_selected",
            inverseJoinColumns = {@JoinColumn(name="customisation_option_id")},
            joinColumns = {@JoinColumn(name="customer_order_id")}
    )
    @JsonView(View.CustomerOrderView.class)
    private List<CustomisationOption> customisationOptions;

    public CustomerOrder(String customerOrderId,
                         double customerOrderPrice,
                         int quantity,
                         String customerOrderRemarks,
                         Orders order,
                         FoodPrice foodPrice,
                         List<CustomisationOption> customisationOptions){
        this.customerOrderId = customerOrderId;
        this.customerOrderPrice = customerOrderPrice;
        this.customerOrderQuantity = quantity;
        this.orderId = order.getOrderId();
        this.order = order;
        this.menuFoodCatId = foodPrice.getMenuFoodCatId();
        this.foodPrice = foodPrice;
        this.customisationOptions = customisationOptions;
        this.customerOrderRemarks = customerOrderRemarks;
    }
}

