package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.net.URL;
import java.util.List;

@Entity
@Data
public class FoodPrice {
    @EmbeddedId
    @JsonView(View.ViewB.class)
    private MenuFoodId menuFoodId;

    @JsonView(View.MainView.class)
    private double price;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    @JsonView(View.MainView.class)
    private boolean availability;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy="foodPrice")
    @JsonView(View.MainView.class)
    private List<FoodCustomisation> foodCustomisations;

    @ManyToOne
    @JoinColumn(name="menu_id", insertable = false, updatable = false)
    @JsonView(View.ViewB.class)
    //@JsonIgnore
    private Menu menu;

    @ManyToOne
    @JoinColumn(name="food_id", insertable = false, updatable = false)
    @JsonView(View.MainView.class)
    //@JsonIgnore
    private Food food;
}
