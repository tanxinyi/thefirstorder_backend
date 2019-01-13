package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.Manager;
import MakaNow.thefirstorder_back.model.View;
import MakaNow.thefirstorder_back.repository.ManagerRepository;
import com.fasterxml.jackson.annotation.JsonView;
import javassist.NotFoundException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class ManagerController {

    @Autowired
    private ManagerRepository managerRepository;

    @PostMapping("/managers")
    @JsonView(View.ManagerView.class)
    public List<Manager> getAllManagers(){
        return (List<Manager>) managerRepository.findAll();
    }

    @RequestMapping(value = "/managers/authenticate", method = RequestMethod.POST, produces = "application/json")
    @JsonView(View.ManagerView.class)
    public @ResponseBody Manager authenticateManager( @RequestBody @Valid AuthenticationDetails authenticationDetails ) throws NotFoundException{
        String managerId = authenticationDetails.getManagerId().toUpperCase();
        String password = authenticationDetails.getPassword();
        Optional<Manager> optionalManager = managerRepository.findById(managerId);
        if(optionalManager.isPresent()){
            Manager manager = optionalManager.get();
            if (manager.getPassword().equals(password)) {
                return manager;
            }
        }else{
            throw new NotFoundException("Manager ID:" + managerId + " does not exist");
        }
        throw new NotFoundException("Manager ID:" + managerId + " does not exist");
    }

    @PostMapping("/managers/{managerId}")
    public String getManagerNameByManagerId(@PathVariable("managerId") String managerId){
        Optional<Manager> optionalManager = managerRepository.findById(managerId);
        if(optionalManager.isPresent()){
            Manager manager = optionalManager.get();
            return manager.getFirstName();
        }else{
            return null;
        }
    }
}

@Data
class AuthenticationDetails{

    private String managerId;
    private String password;
}
