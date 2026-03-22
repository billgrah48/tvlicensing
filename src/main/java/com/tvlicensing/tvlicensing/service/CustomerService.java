package com.tvlicensing.tvlicensing.service;

import com.tvlicensing.tvlicensing.model.Customer;

// Imports our CustomerRepository so we can talk to the database
import com.tvlicensing.tvlicensing.repository.CustomerRepository;

// @Service marks this class as a service component so Spring
// manages it and makes it available throughout the app
import org.springframework.stereotype.Service;

// PasswordEncoder lets us encrypt passwords before saving them
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class CustomerService {

    // The repository that talks to the database
    private final CustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder;

    //Constructor
    public CustomerService(CustomerRepository customerRepository,
                           PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // REGISTER A NEW CUSTOMER
    public void registerCustomer(Customer customer) {

        // Check if a customer with this email already exists
        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            throw new RuntimeException("An account with this email already exists");
        }

        // Encrypt the password before saving it to the database
        String encryptedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encryptedPassword);

        // Save the customer to the database
        customerRepository.save(customer);
    }

}
