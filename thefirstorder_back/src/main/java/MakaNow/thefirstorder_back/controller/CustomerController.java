package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.Customer;
import MakaNow.thefirstorder_back.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api")

public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/authenticate/{email}")
    public String testAuthenticate (@PathVariable("email") String email){
        Optional<Customer> customerOptional = customerRepository.findById(email);
        
        if (customerOptional.isPresent()){
            return customerOptional.get().getFirstName();
        }
        return "Item not found!";
    }
}
