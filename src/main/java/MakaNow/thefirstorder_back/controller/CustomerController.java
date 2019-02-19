package MakaNow.thefirstorder_back.controller;

import MakaNow.thefirstorder_back.model.Customer;
import MakaNow.thefirstorder_back.model.View;
import MakaNow.thefirstorder_back.repository.CustomerRepository;
import com.fasterxml.jackson.annotation.JsonView;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

@RestController
@RequestMapping("/api")
public class CustomerController {
    @Autowired
    private CustomerRepository customerRepository;

    Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @GetMapping("/customers")
    @JsonView(View.CustomerView.class)
    public List<Customer> getAllCustomers() {
        return (List<Customer>) customerRepository.findAll();
    }

    @GetMapping("/customer")
    @JsonView(View.CustomerView.class)
    public Customer getCustomerById(
            @RequestParam String email) throws NotFoundException {

        logger.info("Getting Customer by ID: " + email);
        Optional<Customer> customerOptional = customerRepository.findById(email.toUpperCase());

        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            return customer;
        }
        throw new NotFoundException("Customer ID " + email + " does not exist");
    }

    @GetMapping("/authenticate")
    @JsonView(View.CustomerView.class)
    public Customer authenticate(
            @RequestParam String email,
            @RequestParam String password) throws NotFoundException {


        if (isValidEmailAddress(email.toUpperCase()) && isValidPassword(password)) {
            Optional<Customer> customerOptional = customerRepository.findById(email.toUpperCase());
            if (customerOptional.isPresent()) {
                Customer customer = customerOptional.get();
                if (customer.getCustomerPassword().equalsIgnoreCase(password)) {
                    return customer;
                }
            }
        }
        throw new NotFoundException("User not found!");
    }


    @PostMapping("/customers/add")
    @JsonView(View.CustomerView.class)
    public Customer addNewCustomer(
            @RequestParam String email,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String password,
            @RequestParam(required = false) Date dob,
            @RequestParam(defaultValue = "X") char gender,
            @RequestParam(required = false) String phoneNum,
            @RequestParam(defaultValue = "0") int loyaltyPoints) throws Exception {


        if (!isValidEmailAddress(email.toUpperCase())) {
            throw new Exception("Email not valid.");
        }
        if (!isValidPassword(password)) {
            throw new Exception("Password not valid.");
        }

        if (!isValidName(firstName) || !isValidName(lastName)) {
            throw new Exception("First/Last name not valid.");
        }

        if (!isValidPhoneNum(phoneNum)) {
            throw new Exception("Phone number not valid.");
        }

        Optional<Customer> customerOptional = customerRepository.findById(email);
        if (customerOptional.isPresent()) {
            throw new Exception("Email is in use.");
        }
        Customer customer = new Customer();

        customer.setEmail(email.toUpperCase());
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setCustomerPassword(password.toUpperCase());
//        customer.setDob(dob);
//        customer.setGender(gender);
        customer.setCustomerContactNumber(phoneNum);
        customer.setLoyaltyPoint(loyaltyPoints);


        System.out.println(customer);

        return customerRepository.save(customer);
    }

    @PostMapping("/customer/edit")
    @JsonView(View.CustomerView.class)
    public Customer editProfile(
            @RequestParam String email,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam(required = false) String phoneNum,
            @RequestParam String password) throws Exception {


        if (!isValidEmailAddress(email.toUpperCase())) {
            throw new Exception("Email not valid.");
        }

        if (!isValidPassword(password)) {
            throw new Exception("Password not valid.");
        }

        Customer customer = getCustomerById(email.toUpperCase());

        if (!isValidName(firstName) || !isValidName(lastName)) {
            throw new Exception("First/Last name not valid.");
        }
        if (!isValidPhoneNum(phoneNum)) {
            throw new Exception("Phone number not valid.");
        }



        System.out.println("Before: " + customer.getFirstName());
//        Make changes to existing record
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setCustomerPassword(password.toUpperCase());
        customer.setCustomerContactNumber(phoneNum);

        System.out.println("After: " + customer.getFirstName());
        return customerRepository.save(customer);
    }


    @PostMapping("/customers/{customerId}/updatePoint/{points}")
    @JsonView(View.CustomerView.class)
    public Customer updateLoyaltyPoint(@PathVariable String customerId,
                                       @PathVariable int points) throws NotFoundException {
        Customer customer = getCustomerById(customerId);
        customer.setLoyaltyPoint(points);
        return customerRepository.save(customer);
    }

    private boolean isValidPassword(String password) {
        Pattern p = Pattern.compile("\\w{64}");
        Matcher m = p.matcher(password);
        if (m.matches()) {
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

    private boolean isValidName(String name) {
        Pattern p = Pattern.compile("\\D+");
        Matcher m = p.matcher(name);
        if (m.matches()) {
            return true;
        }
        return false;
    }

    private boolean isValidPhoneNum(String number) {
        Pattern p = Pattern.compile("\\d{8}");
        Matcher m = p.matcher(number);
        if (m.matches()) {
            return true;
        }
        return false;
    }
}

