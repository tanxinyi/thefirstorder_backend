package MakaNow.thefirstorder_back.service;

import MakaNow.thefirstorder_back.model.ActivityLog;
import MakaNow.thefirstorder_back.repository.ActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ActivityLogService {
    @Autowired
    private ActivityLogRepository activityLogRepository;

    public String getNewActivityLogId(){
        List<String> activityLogIds = new ArrayList<>();
        List<ActivityLog> activityLogs = (List<ActivityLog>) activityLogRepository.findAll();

        //For first menu created to avoid index out of bounds error
        if(activityLogs.size() == 0){
            return "AL001";
        }

        for(int i = 0; i < activityLogs.size(); i++){
            ActivityLog activityLog = activityLogs.get(i);
            String activityLogId = activityLog.getActivityLogId();
            activityLogIds.add(activityLogId);
        }
        Collections.sort(activityLogIds);
        String lastActivityLogId = activityLogIds.get(activityLogIds.size()-1);
        int activityLogIdNumber = Integer.parseInt(lastActivityLogId.substring(2));
        int newActivityLogIdNumber = activityLogIdNumber + 1;
        int length = String.valueOf(newActivityLogIdNumber).length();

        if(length == 1) {
            return ("AL00" + newActivityLogIdNumber);
        }
        else if(length == 2){
            return ("AL0" + newActivityLogIdNumber);
        }
        return ("AL" + newActivityLogIdNumber);
    }

    public List<ActivityLog> getActivityLogsByRestaurantId(String restaurantId){
        List<ActivityLog> results = new ArrayList<>();

        List<ActivityLog> activityLogs = (List<ActivityLog>) activityLogRepository.findAll();

        for (int i = 0; i < activityLogs.size(); i++){
            ActivityLog activityLog = activityLogs.get(i);
            String checkCestaurantId = activityLog.getRestaurantId();
            if (checkCestaurantId.equals(restaurantId)){
                results.add(activityLog);
            }
        }
        return results;
    }
}
