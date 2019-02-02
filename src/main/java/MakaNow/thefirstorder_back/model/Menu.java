package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Data
@Table(name="menu")
public class Menu implements Comparable<Menu> {

    @Id
    @JsonView(View.MainView.class)
    private String menuId;

    @JsonView(View.MainView.class)
    @Column(name="restaurant_id")
    private String restaurantId;

    @JsonView(View.MainView.class)
    @Column(name="menu_name")
    private String menuName;

    @JsonView(View.MainView.class)
    private Date menuCreationDate;

    @JsonView(View.MenuView.class)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", insertable = false, updatable = false)
    private Restaurant restaurant;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "menu", orphanRemoval = true)
    @JsonView(View.MenuView.class)
    private List<FoodPrice> foodPrices;

    public int compareTo(Menu another){
        return -1 * this.menuCreationDate.compareTo(another.getMenuCreationDate());
    }
}