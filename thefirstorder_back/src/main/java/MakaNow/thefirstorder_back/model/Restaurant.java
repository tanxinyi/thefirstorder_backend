package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
public class Restaurant {

    @Id
    @JsonView(View.Public.class)
    private String restaurantId;
    @JsonView(View.Public.class)
    private String name;
    @JsonView(View.Public.class)
    private String description;
    @JsonView(View.Public.class)
    private String contactNumber;
    @JsonView(View.Public.class)
    private String street;
    @JsonView(View.Public.class)
    private String postalCode;
    @JsonView(View.Public.class)
    private String cuisine;

    @JsonView(View.Internal.class)
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Menu> menus;

    @JsonView(View.Internal.class)
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SeatingTable> seatingTables;


}
