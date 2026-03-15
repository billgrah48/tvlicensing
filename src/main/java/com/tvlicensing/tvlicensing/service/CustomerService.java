// This file belongs to the service package
package com.tvlicensing.tvlicensing.service;

// Imports our Customer model
import com.tvlicensing.tvlicensing.model.Customer;

// Imports our CustomerRepository so we can talk to the database
import com.tvlicensing.tvlicensing.repository.CustomerRepository;

// @Service marks this class as a service component so Spring
// manages it and makes it available throughout the app
import org.springframework.stereotype.Service;

// PasswordEncoder lets us encrypt passwords before saving them
import org.springframework.security.crypto.password.PasswordEncoder;

// @Service tells Spring Boot this class contains business logic
@Service
public class CustomerService {

    // The repository that talks to the database
    // final means this reference never changes after being set
    private final CustomerRepository customerRepository;

    // The password encoder we configured in SecurityConfig
    private final PasswordEncoder passwordEncoder;

    // CONSTRUCTOR INJECTION
    // Spring automatically provides the repository and encoder
    // here when it creates this service - this is the preferred
    // way to inject dependencies in Spring
    public CustomerService(CustomerRepository customerRepository,
                           PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // REGISTER A NEW CUSTOMER
    // This method is called when someone submits the register form
    public void registerCustomer(Customer customer) {

        // Check if a customer with this email already exists
        // If they do, throw an error back to the controller
        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            throw new RuntimeException("An account with this email already exists");
        }

        // Encrypt the password before saving it to the database
        // We NEVER store plain text passwords - this is critical
        // BCrypt turns "mypassword" into something like:
        // "$2a$10$Xl0yhvzLIaJCDdKBS0Lld.ksK7c2Wss1tbMLYFCE7wHvDuyneF4Iq"
        String encryptedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encryptedPassword);

        // Save the customer to the database
        // The repository handles the actual SQL INSERT statement
        customerRepository.save(customer);
    }

}
