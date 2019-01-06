package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class FoodCategory {
    @Id
    @JsonView(View.MainView.class)
    private String foodCategoryId;

    @JsonView(View.MainView.class)
    private String foodCategoryName;

    @JsonView(View.MainView.class)
    private String foodCategoryImgPath;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy="foodCategory")
    @JsonView(View.CategoryView.class)
    private List<FoodPrice> foodPrices;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy="foodCategory")
    @JsonView(View.CategoryView.class)
    private List<SubCategory> subCategories;
}
