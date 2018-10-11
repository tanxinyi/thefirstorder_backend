package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Food {

    @Id
    private String foodId;
    private String name;
    private String description;
    private String category;

    @ManyToMany
    @JoinTable(name = "food_tag_allocation",
                    joinColumns = { @JoinColumn(name = "food_id") },
                    inverseJoinColumns = { @JoinColumn(name = "food_tag_id") })
    private List<FoodTag> tags;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "food")
    @JsonIgnore
    private List<FoodPrice> foodPrice;
}
