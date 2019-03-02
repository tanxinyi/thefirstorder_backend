package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class SeatingTable {

    @Id
    @JsonView(View.MainView.class)
    private String qrCode;

    @JsonIgnore
    @Column(name="restaurant_id")
    private String restaurantId;

    @JsonView(View.MainView.class)
    private int tableCapacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", insertable=false, updatable = false, nullable = false)
    @JsonView(View.SeatingTableView.class)
    private Restaurant restaurant;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "seatingTable")
    @JsonView(View.SeatingTableView.class)
    private List<OrderSummary> orderSummaries;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "seatingTable")
    @JsonView(View.SeatingTableView.class)
    private List<Orders> orders;
}

