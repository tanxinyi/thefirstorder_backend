package MakaNow.thefirstorder_back.service;

import MakaNow.thefirstorder_back.model.ManagerAllocation;
import MakaNow.thefirstorder_back.repository.ManagerAllocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ManagerAllocationService {

    @Autowired
    private ManagerAllocationRepository managerAllocationRepository;

    public List<ManagerAllocation> findManagerAllocationsByManagerId( String managerId){
        List<ManagerAllocation> results = new ArrayList<>();
        List<ManagerAllocation> managerAllocations = (List<ManagerAllocation>) managerAllocationRepository.findAll();
        for(int i = 0; i < managerAllocations.size(); i++){
            ManagerAllocation managerAllocation = managerAllocations.get(i);
            if (managerAllocation.getManagerAllocationPK().getManagerId().equals(managerId)){
                results.add(managerAllocation);
            }
        }
        return results;
    }
}
