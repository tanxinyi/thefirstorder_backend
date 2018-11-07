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

    @JsonView(View.MainView.class)
    private double subtotal;

    @JsonView(View.MainView.class)
    private String status;

    @JsonIgnore
    @Column(name="qr_code")
    private String qrCode;

    @JsonIgnore
    @Column(name="order_summary_id")
    private String orderSummaryId;

    @ManyToOne
    @JoinColumn(name="qr_code", insertable = false, updatable = false)
    @JsonIgnore
    private SeatingTable seatingTable;

    @ManyToOne
    @JoinColumn(name="order_summary_id", insertable = false, updatable = false)
    @JsonIgnore
    private OrderSummary orderSummary;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "order")
    @JsonIgnore
    private List<CustomerOrder> customerOrders;

    public Orders(String orderId, SeatingTable seatingTable, OrderSummary orderSummary, double subtotal, String status){
        this.orderId = orderId;
        this.seatingTable = seatingTable;
        this.qrCode = seatingTable.getQrCode();
        this.orderSummary = orderSummary;
        this.orderSummaryId = orderSummary.getOrderSummaryId();
        this.subtotal = subtotal;
        this.status = status;
    }
}
