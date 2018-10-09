package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class SeatingTable {

    @Id
    @JsonView(View.Public.class)
    private String qrCode;

    @ManyToOne//(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_Id", nullable = false)
    @JsonView(View.Public.class)
    private Restaurant restaurant;

    @JsonView(View.Public.class)
    private int capacity;
}
