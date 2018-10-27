package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Food {

    @Id
    @JsonView(View.MainView.class)
    private String foodId;
    @JsonView(View.MainView.class)
    private String name;
    @JsonView(View.MainView.class)
    private String description;
    @JsonView(View.MainView.class)
    private String category;
    @JsonView(View.MainView.class)
    private String img_Path;

    @ManyToMany
    @JoinTable(name = "food_tag_allocation",
                    joinColumns = { @JoinColumn(name = "food_id") },
                    inverseJoinColumns = { @JoinColumn(name = "food_tag_id") })
    @JsonView(View.MainView.class)
    private List<FoodTag> tags;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "food")
    @JsonIgnore
    private List<FoodPrice> foodPrice;
}
