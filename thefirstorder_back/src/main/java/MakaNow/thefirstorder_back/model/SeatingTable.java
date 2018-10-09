package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class SeatingTable {

    @Id
    private String qrCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_Id", nullable = false)
    @JsonIgnore
    private Restaurant restaurant;

    private int capacity;
}
