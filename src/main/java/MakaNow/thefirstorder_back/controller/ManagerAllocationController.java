package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.ManagerAllocation;
import MakaNow.thefirstorder_back.repository.ManagerAllocationRepository;
import MakaNow.thefirstorder_back.service.ManagerAllocationService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class ManagerAllocationController {

    @Autowired
    private ManagerAllocationRepository managerAllocationRepository;

    @Autowired
    ManagerAllocationService managerAllocationService;

    @PostMapping("/managerAllocations")
    public List<ManagerAllocation> getAllManagerAllocations(){ return (List<ManagerAllocation>) managerAllocationRepository.findAll(); }

    @GetMapping("/managerAllocations/{managerId}")
    public ResponseEntity<?> getManagerAllocationByManagerId( @PathVariable("managerId") String managerId ){
        List<ManagerAllocation> result;
        result = managerAllocationService.findManagerAllocationsByManagerId(managerId);
        return new ResponseEntity(result, HttpStatus.OK);
    }

}

