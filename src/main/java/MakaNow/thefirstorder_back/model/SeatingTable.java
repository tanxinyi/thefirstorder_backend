package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class SeatingTable {

    @Id
    @JsonView(View.MainView.class)
    private String qrCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_Id", nullable = false)
    @JsonView(View.ViewA.class)
    private Restaurant restaurant;

    @JsonView(View.MainView.class)
    private int capacity;
}
