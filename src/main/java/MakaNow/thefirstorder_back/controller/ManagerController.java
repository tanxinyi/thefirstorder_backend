package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.*;
import MakaNow.thefirstorder_back.repository.AdminRepository;
import MakaNow.thefirstorder_back.repository.ManagerAllocationRepository;
import MakaNow.thefirstorder_back.repository.ManagerRepository;
import MakaNow.thefirstorder_back.repository.RestaurantRepository;
import MakaNow.thefirstorder_back.service.ManagerService;
import com.fasterxml.jackson.annotation.JsonView;
import javassist.NotFoundException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private ManagerService managerService;

    @Autowired
    private ManagerAllocationRepository managerAllocationRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private AdminRepository adminRepository;

    @PostMapping("/managers")
    @JsonView(View.ManagerView.class)
    public List<Manager> getAllManagers(){
        return (List<Manager>) managerRepository.findAll();
    }

    @RequestMapping(value = "/managers/authenticate", method = RequestMethod.POST, produces = "application/json")
    @JsonView(View.ManagerView.class)
    public @ResponseBody ResponseEntity<?> authenticateManager( @RequestBody @Valid AuthenticationDetails authenticationDetails ) throws NotFoundException{
        String managerId = authenticationDetails.getManagerId().toUpperCase();
        String password = authenticationDetails.getPassword();
        Optional<Manager> optionalManager = managerRepository.findById(managerId);
        Optional<Admin> optionalAdmin = adminRepository.findById(managerId);
        if(optionalManager.isPresent()){
            Manager manager = optionalManager.get();
            if (manager.getPassword().equals(password)) {
                return new ResponseEntity(manager, HttpStatus.OK);
            }
        }else if(optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();
            if(admin.getAdminPassword().equals(password)){
                return new ResponseEntity(admin, HttpStatus.OK);
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

    @GetMapping("managers/getManagersByRestaurantId/{restaurantId}")
    @JsonView(View.CustomisationView.class)
    public List<Manager> getManagersByRestaurantId(@PathVariable String restaurantId) throws NotFoundException {
        List<Manager> toReturn = new ArrayList<>();
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
        if(optionalRestaurant.isPresent()) {
            Restaurant restaurant = optionalRestaurant.get();
            List<ManagerAllocation> managerAllocations = restaurant.getManagerAllocations();
            if(managerAllocations.size() != 0){
                for(int i = 0; i < managerAllocations.size(); i++){
                    ManagerAllocation managerAllocation = managerAllocations.get(i);
                    toReturn.add(managerAllocation.getManager());
                }
            }
        }
        return toReturn;
    }

    @DeleteMapping("/manager/deleteManagerAllocation/{managerId}/{restaurantId}")
    public String deleteRestaurant(@PathVariable String managerId, @PathVariable String restaurantId) throws NotFoundException {
        ManagerAllocationPK managerAllocationPK = new ManagerAllocationPK(restaurantId, managerId);
        return managerAllocationRepository.findById(managerAllocationPK)
                .map(managerAllocation -> {
                    managerAllocationRepository.delete(managerAllocation);
                    return "Manager Allocation Deleted Successfully";
                }).orElseThrow(() -> new NotFoundException("Manager Allocation not found."));
    }

    @PostMapping("/manager/addManagerAllocation/{managerId}/{restaurantId}")
    public ResponseEntity<?> addExistingManagerToManagerAllocation(@PathVariable("managerId") String managerId, @PathVariable("restaurantId") String restaurantId) {
        ManagerAllocationPK managerAllocationPK = new ManagerAllocationPK(restaurantId, managerId);
        ManagerAllocation managerAllocation = new ManagerAllocation();
        managerAllocation.setManagerAllocationPK(managerAllocationPK);
        managerAllocation.setRights("CRUD");

        managerAllocationRepository.save(managerAllocation);

        return new ResponseEntity("Manager Allocated Successfully", HttpStatus.OK);
    }

    @PostMapping("/manager/createAndAllocateManager/{restaurantId}/{firstName}/{lastName}/{password}")
    public ResponseEntity<?> createAndAllocateManager(@PathVariable("restaurantId") String restaurantId, @PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName, @PathVariable("password") String password) {

        String newManagerId = managerService.getNewManagerId();

        Manager manager = new Manager();
        manager.setManagerId(newManagerId);
        manager.setFirstName(firstName);
        manager.setLastName(lastName);
        manager.setUsername("username");
        manager.setPassword(password);

        managerRepository.save(manager);

        ManagerAllocationPK managerAllocationPK = new ManagerAllocationPK(restaurantId, newManagerId);
        ManagerAllocation managerAllocation = new ManagerAllocation();
        managerAllocation.setManagerAllocationPK(managerAllocationPK);
        managerAllocation.setRights("CRUD");

        managerAllocationRepository.save(managerAllocation);

        return new ResponseEntity("Manager Created and Allocated Successfully", HttpStatus.OK);
    }
}

@Data
class AuthenticationDetails{

    private String managerId;
    private String password;
}