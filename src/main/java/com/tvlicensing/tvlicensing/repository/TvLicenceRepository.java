package com.tvlicensing.tvlicensing.repository;

import com.tvlicensing.tvlicensing.model.Customer;
import com.tvlicensing.tvlicensing.model.TvLicence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TvLicenceRepository extends JpaRepository<TvLicence, Long> {

    // Find all licences held by a specific customer
    List<TvLicence> findByLicenceHolder(Customer licenceHolder);
}
