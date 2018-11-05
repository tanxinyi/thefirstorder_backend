package MakaNow.thefirstorder_back.model;

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

    @JsonView(View.MainView.class)
    private double subtotal;

    @JsonView(View.MainView.class)
    private String status = "Pending";

    @ManyToOne
    @JoinColumn(name="qr_code", insertable = false, updatable = false)
    @JsonView(View.ViewA.class)
    private SeatingTable seatingTable;

    @ManyToOne
    @JoinColumn(name="order_summary_id", insertable = false, updatable = false)
    @JsonView(View.ViewA.class)
    private OrderSummary orderSummary;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "order")
    @JsonView(View.ViewA.class)
    private List<CustomerOrder> customerOrders;
}
