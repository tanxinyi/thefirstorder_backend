package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class FoodPrice {
    @EmbeddedId
    private MenuFoodId menuFoodId;

    private Double price;
    private boolean availability;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy="foodPrice")
    private List<FoodCustomisation> foodCustomisations;

    @ManyToOne
    //@JsonIgnore
    private Menu menu;

    @ManyToOne
    //@JsonIgnore
    private Food food;
}
