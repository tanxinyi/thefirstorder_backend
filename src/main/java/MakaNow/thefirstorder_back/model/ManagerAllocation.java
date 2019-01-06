package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name="manager_allocation")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ManagerAllocation {

    @EmbeddedId
    @JsonView(View.MainView.class)
    private ManagerAllocationPK managerAllocationPK;

    @JsonView(View.MainView.class)
    @Column(name="rights")
    private String rights;

    @JsonView(View.ManagerAllocationView.class)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", insertable = false, updatable = false)
    private Manager manager;

    @JsonView(View.ManagerAllocationView.class)
    @ManyToOne
    @JoinColumn(name = "restaurant_id", insertable = false, updatable = false)
    private Restaurant restaurant;
}
