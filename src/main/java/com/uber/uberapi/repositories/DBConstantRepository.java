package com.uber.uberapi.repositories;

import com.uber.uberapi.models.DBConstant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DBConstantRepository extends JpaRepository<DBConstant, Long> {
}
