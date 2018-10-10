package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
public class Restaurant {

    @Id
    @JsonView(View.MainView.class)
    private String restaurantId;
    @JsonView(View.MainView.class)
    private String name;
    @JsonView(View.MainView.class)
    private String description;
    @JsonView(View.MainView.class)
    private String contactNumber;
    @JsonView(View.MainView.class)
    private String street;
    @JsonView(View.MainView.class)
    private String postalCode;
    @JsonView(View.MainView.class)
    private String cuisine;

    @JsonView(View.ViewB.class)
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Menu> menus;

    @JsonView(View.ViewB.class)
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SeatingTable> seatingTables;


}
