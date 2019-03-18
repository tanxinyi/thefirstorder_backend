package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name="orders")
public class Orders {
    @Id
    @JsonView(View.MainView.class)
    private String orderId;

    @JsonView(View.MainView.class)
    private double totalAmount;

    @JsonView(View.MainView.class)
    private String orderStatus;

    @JsonView(View.MainView.class)
    private String paymentStatus;

    @JsonView(View.MainView.class)
    private String modeOfPayment;

    @JsonView(View.MainView.class)
    private Date orderDate;

    @JsonIgnore
    private String email;

    @JsonIgnore
    @Column(name="qr_code")
    private String qrCode;

    @JsonView(View.MainView.class)
    private String token;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "order")
    @JsonView(View.OrdersView.class)
    private List<CustomerOrder> customerOrders;

    @ManyToOne
    @JoinColumn(name="qr_code", insertable = false, updatable = false)
    @JsonView(View.OrdersView.class)
    private SeatingTable seatingTable;

    @ManyToOne
    @JoinColumn(name="email", insertable = false, updatable = false)
    @JsonView(View.OrderSummaryView.class)
    private Customer customer;

    public Orders(String orderId,
                  double totalAmount,
                  String orderStatus,
                  String paymentStatus,
                  String modeOfPayment,
                  Customer customer,
                  SeatingTable seatingTable){
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
        this.customer = customer;
        this.email = customer.getEmail();
        this.paymentStatus = paymentStatus;
        this.totalAmount = totalAmount;
        this.orderDate = new Date();
        this.modeOfPayment = modeOfPayment;
        this.qrCode = seatingTable.getQrCode();
        this.seatingTable = seatingTable;
    }
}

