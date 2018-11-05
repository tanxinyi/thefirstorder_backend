package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

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
    private String paymentStatus = "Pending";

    @JsonView(View.MainView.class)
    private double totalAmount;

    @JsonView(View.MainView.class)
    private Date summaryDate;

    @JsonView(View.MainView.class)
    private String modeOfPayment;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "orderSummary")
    @JsonView(View.ViewB.class)
    private List<Orders> orders;

    @ManyToOne
    @JoinColumn(name="email", insertable = false, updatable = false)
    @JsonView(View.ViewB.class)
    private Customer customer;
}
