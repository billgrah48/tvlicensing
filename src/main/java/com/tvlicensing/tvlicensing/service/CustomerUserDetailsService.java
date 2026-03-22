package com.tvlicensing.tvlicensing.service;

import com.tvlicensing.tvlicensing.model.Customer;

// Imports our repository to look up customers in the database
import com.tvlicensing.tvlicensing.repository.CustomerRepository;

// Spring Security imports
// UserDetails is Spring Security's representation of a logged in user
// UserDetailsService is the interface we must implement
// UsernameNotFoundException is thrown when no user is found
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

// Marks this as a Spring managed service
import org.springframework.stereotype.Service;

// @Service tells Spring to manage this class
@Service
public class CustomerUserDetailsService implements UserDetailsService {

    // The repository that looks up customers in the database
    private final CustomerRepository customerRepository;

    // Spring injects the repository automatically
    public CustomerUserDetailsService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Spring Security calls this automatically when someone tries to log in
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        // Look up the customer by email in the database
        Customer customer = customerRepository.findByEmail(email)
                // If no customer found with that email, throw this error
                // Spring Security catches this and triggers login failure
                .orElseThrow(() -> new UsernameNotFoundException(
                        "No account found with email: " + email));

        // Spring Security checks the password automatically
        return User.builder()
                .username(customer.getEmail())
                .password(customer.getPassword())
                .roles("CUSTOMER")
                .build();
    }
}
