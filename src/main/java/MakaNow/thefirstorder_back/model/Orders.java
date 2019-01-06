package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Orders {
    @Id
    @JsonView(View.MainView.class)
    private String orderId;

    @JsonIgnore
    @Column(name="order_summary_id")
    private String orderSummaryId;

    @JsonView(View.MainView.class)
    private double subtotal;

    @ManyToOne
    @JoinColumn(name="order_summary_id", insertable = false, updatable = false)
    @JsonView(View.OrdersView.class)
    private OrderSummary orderSummary;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "order")
    @JsonView(View.OrdersView.class)
    private List<CustomerOrder> customerOrders;

    public Orders(String orderId, OrderSummary orderSummary, double subtotal){
        this.orderId = orderId;
        this.orderSummary = orderSummary;
        this.orderSummaryId = orderSummary.getOrderSummaryId();
        this.subtotal = subtotal;
    }
}

