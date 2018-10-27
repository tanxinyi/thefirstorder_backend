package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
@Data
public class CustomerOrder {
    @Id
    @JsonView(View.MainView.class)
    private String customerOrderId;

    @JsonView(View.MainView.class)
    private int quantity;

    @ManyToOne
    @JoinColumn(name="order_id", insertable = false, updatable = false)
    @JsonView(View.ViewB.class)
    private Orders order;

    @ManyToOne
    @JoinColumn(name="food_id", insertable = false, updatable = false)
    @JsonView(View.ViewB.class)
    private Food food;

    @JsonView(View.MainView.class)
    private String remarks;
}
