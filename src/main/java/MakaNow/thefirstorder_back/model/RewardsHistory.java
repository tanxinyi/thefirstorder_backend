package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class RewardsHistory {

    @Id
    @JsonView(View.MainView.class)
    private String rewardsHistoryId;

    @JsonView(View.MainView.class)
    private String email;

    @JsonView(View.MainView.class)
    @Column(name = "rewards_id")
    private String rewardsId;

    @JsonView(View.MainView.class)
    private int currentLoyaltyPoints;

    @JsonView(View.MainView.class)
    private Date redeemRewardsDate;

    @ManyToOne
    @JoinColumn(name = "email", insertable = false, updatable = false)
    @JsonView(View.RewardsHistoryView.class)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "rewards_id", insertable = false, updatable = false)
    @JsonView(View.RewardsHistoryView.class)
    private Rewards rewards;

}
