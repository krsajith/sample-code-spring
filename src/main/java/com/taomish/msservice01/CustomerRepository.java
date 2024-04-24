package com.taomish.msservice01;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Customer> findById(UUID integer);
}
