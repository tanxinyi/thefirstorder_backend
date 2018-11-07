package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.net.URL;
import java.util.List;

@Entity
@Data
@Table(name="food_price")
public class FoodPrice {

    @EmbeddedId
    @JsonView(View.ViewB.class)
    private MenuFoodId menuFoodId;

    @JsonView(View.MainView.class)
    @Column(name="price")
    private double price;

    @Column(name="availability", nullable = false, columnDefinition = "TINYINT(1)")
    @JsonView(View.MainView.class)
    private boolean availability;

    @ManyToOne
    @JoinColumn(name="menu_id", insertable = false, updatable = false)
    @JsonView(View.ViewB.class)
    private Menu menu;

    @ManyToOne
    @JoinColumn(name="food_id", insertable = false, updatable = false)
    @JsonView(View.ViewB.class)
    private Food food;
}
