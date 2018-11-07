package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name="category")
public class Category {

    @Id
    @JsonView(View.MainView.class)
    @Column(name="category_id")
    private String categoryId;

    @JsonView(View.MainView.class)
    @Column(name="category_name")
    private String categoryName;

    @JsonView(View.MainView.class)
    @Column(name="cat_img")
    private String categoryImage;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy="category")
    @JsonView(View.ViewB.class)
    private List<Food> foods;
}
