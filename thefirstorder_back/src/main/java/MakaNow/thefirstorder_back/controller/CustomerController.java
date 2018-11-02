package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.Customer;
import MakaNow.thefirstorder_back.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javassist.NotFoundException;
import sun.misc.Request;

import java.util.*;

@RestController
@RequestMapping("/api")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/customers")
    public List<Customer> getAllCustomers(){
        return (List<Customer>) customerRepository.findAll();

    }

    @GetMapping("/customers/{email}")
    public Customer getCustomer(@PathVariable String email) throws NotFoundException{
        Optional<Customer> customerOptional = customerRepository.findById(email);
        if (customerOptional.isPresent()){
            Customer customer = customerOptional.get();
                return customer;
        }
        throw new NotFoundException("User not found!");
    }

    @GetMapping("/authenticate")
    public Customer authenticate (@RequestParam String email,
                                  @RequestParam String password) throws NotFoundException {
        Optional<Customer> customerOptional = customerRepository.findById(email);
        if (customerOptional.isPresent()){
            Customer customer = customerOptional.get();
            if(customer.getPassword().equals(password)){
                return customer;
            }
            return null;
        }
        throw new NotFoundException("User not found!");
    }


    @PostMapping("/customers")
    public boolean addNewCustomer(@RequestParam String email,
                                  @RequestParam String firstName,
                                  @RequestParam String lastName,
                                  @RequestParam String password,
                                  @RequestParam (required=false) Date dob,
                                  @RequestParam (defaultValue="X") char gender,
                                  @RequestParam (required=false) String phoneNum,
                                  @RequestParam (defaultValue="0") int loyaltyPoints) throws Exception{

        Optional<Customer> customerOptional = customerRepository.findById(email);
        if(customerOptional.isPresent()){
            throw new Exception("Email is in use.");
        }

        Customer customer = new Customer();

        customer.setEmail(email);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setPassword(password);
        customer.setDob(dob);
        customer.setGender(gender);
        customer.setPhoneNum(phoneNum);
        customer.setLoyaltyPoint(loyaltyPoints);
        customerRepository.save(customer);
        return true;
    }

}
