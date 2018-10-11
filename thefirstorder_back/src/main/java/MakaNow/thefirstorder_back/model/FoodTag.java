package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class FoodTag {

    @Id
    @JsonView(View.MainView.class)
    private String foodTagId;

    @JsonView(View.MainView.class)
    private String description;

    @ManyToMany(fetch = FetchType.LAZY,
                cascade ={
                        CascadeType.PERSIST,
                        CascadeType.MERGE
                }, mappedBy = "tags")
    @JsonIgnore
    private List<Food> foods;
}
