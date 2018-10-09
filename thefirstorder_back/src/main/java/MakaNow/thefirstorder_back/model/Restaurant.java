package MakaNow.thefirstorder_back.model;

import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
public class Restaurant {

    @Id
    private String restaurantId;
    private String name;
    private String description;
    private String contactNumber;
    private String street;
    private String postalCode;
    private String cuisine;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Menu> menus;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SeatingTable> seatingTables;

}
