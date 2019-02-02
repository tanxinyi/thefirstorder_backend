package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Admin {

    @Id
    @JsonView(View.MainView.class)
    @Column(name="admin_id")
    private String adminId;

    @JsonView(View.MainView.class)
    @Column(name="admin_password")
    private String adminPassword;

    @JsonView(View.MainView.class)
    @Column(name="convert_to_points")
    private int moneyToPointsConversionRate;

    @JsonView(View.MainView.class)
    @Column(name="convert_from_points")
    private int pointsToMoneyConversionRate;

    @JsonView(View.MainView.class)
    @Column(name="stripe_token")
    private String stripe_token;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "admin")
    @JsonView(View.RestaurantView.class)
    private List<Restaurant> restaurants;
}

