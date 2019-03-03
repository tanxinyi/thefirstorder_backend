package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Customer {
    @Id
    @JsonView(View.MainView.class)
    private String email;

    @JsonView(View.MainView.class)
    private String firstName;

    @JsonView(View.MainView.class)
    private String lastName;

    @JsonView(View.MainView.class)
    private String customerPassword;

    @JsonView(View.MainView.class)
    private String customerContactNumber;

    @JsonView(View.MainView.class)
    private int loyaltyPoint;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "customer")
    @JsonView(View.CustomerView.class)
    private List<OrderSummary> orderSummaries;

//    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "customer")
//    @JsonView(View.CustomerView.class)
//    private List<RewardsHistory> rewardsHistoryList;
}

