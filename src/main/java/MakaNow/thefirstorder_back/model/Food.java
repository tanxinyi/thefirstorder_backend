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
    private String foodId;

    @JsonView(View.MainView.class)
    private String foodName;

    @JsonView(View.MainView.class)
    private String foodDescription;

    @Lob
    @JsonView(View.MainView.class)
    private byte[] foodImgPath;

    @JsonView(View.FoodView.class)
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "food", orphanRemoval = true)
    private List<FoodPrice> foodPrices;

}
