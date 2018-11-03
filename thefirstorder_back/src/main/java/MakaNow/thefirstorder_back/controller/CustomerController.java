package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.Customer;
import MakaNow.thefirstorder_back.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javassist.NotFoundException;
import sun.misc.Request;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        return null;
    }

    @GetMapping("/authenticate")
    public Customer authenticate (@RequestParam String email,
                                  @RequestParam String password) throws NotFoundException {

        if (isValidEmailAddress(email) && isValidPassword(password)) {
            //System.out.println("Does not fit");

            Optional<Customer> customerOptional = customerRepository.findById(email);
            if (customerOptional.isPresent()) {
                Customer customer = customerOptional.get();
                if (customer.getPassword().equals(password)) {
                    return customer;
                }
            }
        }
        throw new NotFoundException("User not found!");
    }


    @PostMapping("/customers/add")
    public boolean addNewCustomer(@RequestParam String email,
                                  @RequestParam String firstName,
                                  @RequestParam String lastName,
                                  @RequestParam String password,
                                  @RequestParam (required=false) Date dob,
                                  @RequestParam (defaultValue="X") char gender,
                                  @RequestParam (required=false) String phoneNum,
                                  @RequestParam (defaultValue="0") int loyaltyPoints) throws Exception {

        if (!isValidEmailAddress(email)) {
            throw new Exception("Email not valid.");
        }
        if(!isValidPassword(password)){
            throw new Exception("Password not valid.");
        }

        if(!isValidName(firstName) || !isValidName(lastName)){
            throw new Exception("First/Last name not valid.");
        }

        Optional<Customer> customerOptional = customerRepository.findById(email);
        if (customerOptional.isPresent()) {
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

    private boolean isValidPassword(String password){
        Pattern p = Pattern.compile("\\w{8,}");
        Matcher m = p.matcher(password);
        if (m.matches()){
            return true;
        }
        return false;
    }

    private boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    private boolean isValidName (String name) {
        Pattern p = Pattern.compile("\\D+");
        Matcher m = p.matcher(name);
        if (m.matches()){
            return true;
        }
        return false;
    }
}
