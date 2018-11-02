package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.springframework.lang.Nullable;

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
    private String password;

    @Nullable
    @JsonView(View.MainView.class)
    private char gender;

    @Nullable
    @JsonView(View.MainView.class)
    @Temporal(TemporalType.DATE)
    private Date dob;

    @Nullable
    @JsonView(View.MainView.class)
    private String phoneNum;

    @Nullable
    @JsonView(View.MainView.class)
    private int loyaltyPoint;

//    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "customer")
//    @JsonView(View.ViewA.class)
//    private List<OrderSummary> orderSummaries;
}
