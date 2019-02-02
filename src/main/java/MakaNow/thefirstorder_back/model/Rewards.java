//package MakaNow.thefirstorder_back.model;
//
//import com.fasterxml.jackson.annotation.JsonView;
//import lombok.Data;
//
//import javax.persistence.*;
//import java.util.Date;
//import java.util.List;
//
//@Entity
//@Data
//public class Rewards {
//    @Id
//    @JsonView(View.MainView.class)
//    private String rewardsId;
//
//    @JsonView(View.MainView.class)
//    @Column(name="restaurant_id")
//    private String restaurantId;
//
//    @JsonView(View.MainView.class)
//    private Date startDatetime;
//
//    @JsonView(View.MainView.class)
//    private Date endDatetime;
//
//    @JsonView(View.MainView.class)
//    private int allocatedPoints;
//
//    @JsonView(View.MainView.class)
//    private String rewardsDescription;
//
//    @ManyToOne
//    @JoinColumn(name="restaurant_id", insertable = false, updatable = false)
//    @JsonView(View.RewardsView.class)
//    private Restaurant restaurant;
//
//    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "rewards")
//    @JsonView(View.RewardsView.class)
//    private List<RewardsHistory> rewardsHistoryList;
//
//}
