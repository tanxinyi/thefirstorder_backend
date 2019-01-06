package MakaNow.thefirstorder_back.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Data
@Table(name="manager")
public class Manager implements Serializable {

    @Id
    @JsonView(View.MainView.class)
    @Column(name = "manager_id")
    private String managerId;

    @JsonView(View.MainView.class)
    @Column(name = "first_name")
    private String firstName;

    @JsonView(View.MainView.class)
    @Column(name = "last_name")
    private String lastName;

    @JsonView(View.MainView.class)
    @Column(name = "manager_username")
    private String username;

    @JsonView(View.MainView.class)
    @Column(name = "manager_password")
    private String password;

    @JsonView(View.ManagerView.class)
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL},mappedBy = "manager")
    private List<ManagerAllocation> managerAllocations;

    @JsonView(View.ManagerView.class)
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL},mappedBy = "manager")
    private List<ActivityLog> activityLogs;

}