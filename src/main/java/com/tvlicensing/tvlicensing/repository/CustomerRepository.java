package com.tvlicensing.tvlicensing.repository;

// Imports our Customer model so this repository knows
// what type of data it is working with
import com.tvlicensing.tvlicensing.model.Customer;

// JpaRepository gives us all the standard database methods
// for free - save, find, delete etc.
import org.springframework.data.jpa.repository.JpaRepository;

// Optional is a Java wrapper that handles the case where
// a customer might not be found - safer than returning null
import java.util.Optional;

// JpaRepository<Customer, Long> means:
// - This repository works with Customer objects
// - The primary key (ID) is of type Long
// We do not need to write anything inside this interface
// for the standard methods - Spring generates them automatically
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // This is a custom method - Spring reads the method name
    // and automatically generates the SQL to find a customer
    // by their email address
    // We will use this when a customer tries to log in
    Optional<Customer> findByEmail(String email);
}

