package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class CustomerOrder {
    @Id
    @JsonView(View.MainView.class)
    private String customerOrderId;

    @JsonView(View.MainView.class)
    private int quantity;

    @JsonView(View.MainView.class)
    @Column(name="order_id")
    private String orderId;

    @JsonView(View.MainView.class)
    @Column(name="food_id")
    private String foodId;

    @ManyToOne
    @JoinColumn(name="order_id", insertable = false, updatable = false)
    @JsonIgnore
    private Orders order;

    @ManyToOne
    @JoinColumn(name="food_id", insertable = false, updatable = false)
    @JsonIgnore
    private Food food;

    @JsonView(View.MainView.class)
    private String remarks;

    public CustomerOrder(String customerOrderId, int quantity, Orders order, Food food, String remarks){
        this.customerOrderId = customerOrderId;
        this.quantity = quantity;
        this.orderId = order.getOrderId();
        this.order = order;
        this.foodId = food.getFoodId();
        this.food = food;
        this.remarks = remarks;
    }
}
