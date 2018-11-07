package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name="food")
public class Food {

    @Id
    @JsonView(View.MainView.class)
    @Column(name="food_id")
    private String foodId;

    @JsonView(View.MainView.class)
    @Column(name="name")
    private String name;

    @JsonView(View.MainView.class)
    @Column(name="description")
    private String description;

    @JsonView(View.MainView.class)
    @Column(name="img_path")
    private String imgPath;

    @JsonView(View.MainView.class)
    @Column(name="category_id")
    private String categoryId;

    @JsonView(View.ViewA.class)
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "food")
    private List<FoodPrice> foodPrices;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id", insertable = false, updatable = false)
    //@JsonView(View.ViewA.class)
    @JsonIgnore
    private Category category;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "food")
    //@JsonView(View.ViewA.class)
    @JsonIgnore
    private List<CustomerOrder> customerOrders;
}
