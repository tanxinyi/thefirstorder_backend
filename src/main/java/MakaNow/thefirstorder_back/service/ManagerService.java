package MakaNow.thefirstorder_back.service;

import MakaNow.thefirstorder_back.model.Manager;
import MakaNow.thefirstorder_back.model.ManagerAllocation;
import MakaNow.thefirstorder_back.model.Restaurant;
import MakaNow.thefirstorder_back.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ManagerService {

    @Autowired
    private ManagerRepository managerRepository;

    public String getNewManagerId(){
        List<String> managerIds = new ArrayList<>();
        List<Manager> managers = (List<Manager>) managerRepository.findAll();

        //For first menu created to avoid index out of bounds error
        if(managers.size() == 0){
            return "MA001";
        }

        for(int i = 0; i < managers.size(); i++){
            Manager manager = managers.get(i);
            String managerId = manager.getManagerId();
            managerIds.add(managerId);
        }
        Collections.sort(managerIds);
        String lastManagerId = managerIds.get(managerIds.size()-1);
        int managerIdNumber = Integer.parseInt(lastManagerId.substring(2));
        int newManagerIdNumber = managerIdNumber + 1;
        int length = String.valueOf(newManagerIdNumber).length();

        if(length == 1) {
            return ("MA00" + newManagerIdNumber);
        }
        else if(length == 2){
            return ("MA0" + newManagerIdNumber);
        }
        return ("MA" + newManagerIdNumber);
    }
}
