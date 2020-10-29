package com.uber.uberapi.repositories;

import com.uber.uberapi.models.Account;
import com.uber.uberapi.models.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findFirstByAccount_Username(String username);
}
