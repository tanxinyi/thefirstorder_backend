package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name="sub_category")
public class SubCategory {

    @Id
    @JsonView(View.MainView.class)
    @Column(name="sub_category_id")
    private String subCategoryId;

    @JsonView(View.MainView.class)
    @Column(name="food_category_id")
    private String categoryId;

    @JsonView(View.MainView.class)
    @Column(name="sub_category_name")
    private String subCategoryName;

    @JsonView(View.MainView.class)
    @Column(name="sub_category_img_path")
    private String subCategoryImage;

    @ManyToOne
    @JoinColumn(name="food_category_id", insertable = false, updatable = false)
    @JsonView(View.SubCategoryView.class)
    private FoodCategory foodCategory;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL},mappedBy = "subFoodCategory")
    @JsonView(View.SubCategoryView.class)
    private List<FoodPrice> foodPrices;
}
