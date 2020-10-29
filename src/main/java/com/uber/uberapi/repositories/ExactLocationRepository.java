package com.uber.uberapi.repositories;

import com.uber.uberapi.models.ExactLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExactLocationRepository extends JpaRepository<ExactLocation, Long> {
}
