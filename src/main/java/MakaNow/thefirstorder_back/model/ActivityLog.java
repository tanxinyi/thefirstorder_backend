package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name="activity_log")
public class ActivityLog {

    @Id
    @JsonView(View.MainView.class)
    @Column(name="activity_log_id")
    private String activityLogId;

    @JsonView(View.MainView.class)
    @Column(name="manager_id")
    private String managerId;

    @JsonView(View.MainView.class)
    @Column(name="restaurant_id")
    private String restaurantId;

    @JsonView(View.MainView.class)
    @Column(name="description")
    private String description;

    @JsonView(View.MainView.class)
    @Column(name="change_timestamp")
    private Timestamp changeTimeStamp;

    @JsonView(View.ViewD.class)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", insertable = false, updatable = false)
    private Manager manager;

    @JsonView(View.ViewD.class)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", insertable = false, updatable = false)
    private Restaurant restaurant;
}
