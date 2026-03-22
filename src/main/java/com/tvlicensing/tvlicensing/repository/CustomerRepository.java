package com.tvlicensing.tvlicensing.repository;

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

    // Spring reads the method nameand automatically generates the SQL to find a customer
    Optional<Customer> findByEmail(String email);
}

