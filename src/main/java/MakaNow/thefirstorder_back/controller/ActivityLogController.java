package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.ActivityLog;
import MakaNow.thefirstorder_back.model.View;
import MakaNow.thefirstorder_back.repository.ActivityLogRepository;
import MakaNow.thefirstorder_back.service.ActivityLogService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class ActivityLogController {

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Autowired
    private ActivityLogService activityLogService;

    @PostMapping("/activityLogs")
    public List<ActivityLog> getAllActivityLogs(){ return (List<ActivityLog>) activityLogRepository.findAll();
    }

    @PostMapping("/activityLogs/getNewActivityLogId")
    public String getNewActivityLogId(){
        return activityLogService.getNewActivityLogId();
    }

    @PostMapping("/activityLogs/{restaurantId}")
    @JsonView(View.ActivityLogView.class)
    public List<ActivityLog> getActivityLogsByRestaurantId(@PathVariable("restaurantId") String restaurantId){
        List<ActivityLog> toReturn = activityLogService.getActivityLogsByRestaurantId(restaurantId);
        Collections.reverse(toReturn);
        return toReturn;
    }
}

