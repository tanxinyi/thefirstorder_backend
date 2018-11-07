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
    @Column(name="name")
    private String name;

    @JsonView(View.MainView.class)
    @Column(name="description")
    private String description;

    @JsonView(View.MainView.class)
    @Column(name="contact_number")
    private String contactNumber;

    @JsonView(View.MainView.class)
    @Column(name="street")
    private String street;

    @JsonView(View.MainView.class)
    @Column(name="postal_code")
    private String postalCode;

    @JsonView(View.MainView.class)
    @Column(name="cuisine")
    private String cuisine;

    @JsonView(View.ViewB.class)
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Menu> menus;

    @JsonView(View.ViewB.class)
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SeatingTable> seatingTables;

    @JsonView(View.ViewB.class)
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL},mappedBy = "restaurant")
    private List<ManagerAllocation> managerAllocations;

    @JsonView(View.ViewB.class)
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL},mappedBy = "restaurant")
    private List<ActivityLog> activityLogs;
}
