package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.hibernate.criterion.Order;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class OrderSummary {
    @Id
    @JsonView(View.MainView.class)
    private String orderSummaryId;

    @JsonIgnore
    private String email;

    @JsonIgnore
    @Column(name="qr_code")
    private String qrCode;

    @JsonView(View.MainView.class)
    private String paymentStatus;

    @JsonView(View.MainView.class)
    private double totalAmount;

    @JsonView(View.MainView.class)
    private Date orderSummaryDate;

    @JsonView(View.MainView.class)
    private String modeOfPayment;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "orderSummary")
    @JsonView(View.OrderSummaryView.class)
    private List<Orders> orders;

    @ManyToOne
    @JoinColumn(name="email", insertable = false, updatable = false)
    @JsonView(View.OrderSummaryView.class)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name="qr_code", insertable = false, updatable = false)
    @JsonView(View.OrderSummaryView.class)
    private SeatingTable seatingTable;

    public OrderSummary(
            String orderSummaryId,
            Customer customer,
            String paymentStatus,
            double totalAmount,
            Date summaryDate,
            String modeOfPayment,
            SeatingTable seatingTable){
        this.orderSummaryId = orderSummaryId;
        this.customer = customer;
        this.email = customer.getEmail();
        this.paymentStatus = paymentStatus;
        this.totalAmount = totalAmount;
        this.orderSummaryDate = summaryDate;
        this.modeOfPayment = modeOfPayment;
        this.qrCode = seatingTable.getQrCode();
        this.seatingTable = seatingTable;
    }
}
