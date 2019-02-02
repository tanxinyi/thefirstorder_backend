package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.Admin;
import MakaNow.thefirstorder_back.model.View;
import MakaNow.thefirstorder_back.repository.AdminRepository;
import com.fasterxml.jackson.annotation.JsonView;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    Logger logger = LoggerFactory.getLogger(AdminController.class);

    @PostMapping("/admins")
    @JsonView(View.AdminView.class)
    public List<Admin> getAllAdmins(){
        logger.info("Getting All Admins");
        return (List<Admin>) adminRepository.findAll();
    }

    @GetMapping("/admins/{adminId}/conversion_rates")
    public List<Integer> getPointsConversion(@PathVariable String adminId) throws NotFoundException {
        logger.info("Getting Conversion Rates for Admin " + adminId);
        Optional<Admin> admin = adminRepository.findById(adminId);
        if(admin.isPresent()){
            List<Integer> output = new ArrayList<>();
            output.add(admin.get().getPointsToMoneyConversionRate());
            output.add(admin.get().getMoneyToPointsConversionRate());
            return output;
        }
        throw new NotFoundException("Admin Id " + adminId + " does not exist!");
    }
}

