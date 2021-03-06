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
    @Column(name="admin_id")
    private String adminId;

    @JsonView(View.MainView.class)
    private String restaurantName;

    @JsonView(View.MainView.class)
    private String restaurantDescription;

    @JsonView(View.MainView.class)
    private String restaurantContactNumber;

    @JsonView(View.MainView.class)
    private String restaurantOpeningHours;

    @JsonView(View.MainView.class)
    private String building;

    @JsonView(View.MainView.class)
    private String street;

    @JsonView(View.MainView.class)
    private String postalCode;

    @JsonView(View.MainView.class)
    private String cuisine;

    @JsonView(View.MainView.class)
    private String restaurantPriceRange;

    @JsonView(View.MainView.class)
    @Column(name="convert_to_points")
    private double moneyToPointsConversionRate;

    @JsonView(View.MainView.class)
    @Column(name="convert_from_points")
    private double pointsToMoneyConversionRate;

    @JsonView(View.MainView.class)
    @Column(columnDefinition = "TINYINT(1)")
    private boolean gst;

    @JsonView(View.MainView.class)
    @Column(columnDefinition = "TINYINT(1)")
    private boolean serviceCharge;

    @Lob
    @JsonView(View.MainView.class)
    private byte[] restaurantImgPath;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", insertable = false, updatable = false)
    private Admin admin;

//    @JsonView(View.RestaurantView.class)
//    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "restaurant")
//    private List<Rewards> rewardsList;

    public boolean getGst(){
        return this.gst;
    }

    public boolean getServiceCharge(){
        return this.serviceCharge;
    }
}
