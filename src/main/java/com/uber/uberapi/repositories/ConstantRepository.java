package com.uber.uberapi.repositories;

import com.uber.uberapi.models.Constant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConstantRepository extends JpaRepository<Constant, Long> {
}
