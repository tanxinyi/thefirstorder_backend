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
    private String img_Path;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "food")
    @JsonIgnore
    private List<FoodPrice> foodPrice;

    @ManyToOne
    @JoinColumn(name="category_id", insertable = false, updatable = false)
    //@JsonView(View.ViewA.class)
    @JsonIgnore
    private Category category;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "food")
    //@JsonView(View.ViewA.class)
    @JsonIgnore
    private List<CustomerOrder> customerOrders;
}
