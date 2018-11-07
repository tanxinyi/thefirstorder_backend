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

    @JsonView(View.MainView.class)
    private String paymentStatus;

    @JsonView(View.MainView.class)
    private double totalAmount;

    @JsonView(View.MainView.class)
    private Date summaryDate;

    @JsonView(View.MainView.class)
    private String modeOfPayment;

    @JsonIgnore
    private String email;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "orderSummary")
    @JsonIgnore
    private List<Orders> orders;

    @ManyToOne
    @JoinColumn(name="email", insertable = false, updatable = false)
    @JsonIgnore
    private Customer customer;

    public OrderSummary(String orderSummaryId, Customer customer, String paymentStatus, double totalAmount, Date summaryDate, String modeOfPayment){
        this.orderSummaryId = orderSummaryId;
        this.customer = customer;
        this.email = customer.getEmail();
        this.paymentStatus = paymentStatus;
        this.totalAmount = totalAmount;
        this.summaryDate = summaryDate;
        this.modeOfPayment = modeOfPayment;
    }
}
