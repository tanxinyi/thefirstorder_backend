package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
@Table(name="restaurant")
public class Restaurant {

    @Id
    @JsonView(View.MainView.class)
    @Column(name="restaurant_id")
    private String restaurantId;

    @JsonView(View.MainView.class)
    private String restaurantName;

    @JsonView(View.MainView.class)
    private String restaurantDescription;

    @JsonView(View.MainView.class)
    private String restaurantContactNumber;

    @JsonView(View.MainView.class)
    private String street;

    @JsonView(View.MainView.class)
    private String postalCode;

    @JsonView(View.MainView.class)
    private String cuisine;

    @JsonView(View.MainView.class)
    private String restaurantPriceRange;

    @JsonView(View.RestaurantView.class)
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Menu> menus;

    @JsonView(View.RestaurantView.class)
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SeatingTable> seatingTables;

    @JsonView(View.RestaurantView.class)
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL},mappedBy = "restaurant")
    private List<ManagerAllocation> managerAllocations;

    @JsonView(View.RestaurantView.class)
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL},mappedBy = "restaurant")
    private List<ActivityLog> activityLogs;

    @JsonView(View.RestaurantView.class)
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "restaurant")
    private List<Rewards> rewardsList;
}
