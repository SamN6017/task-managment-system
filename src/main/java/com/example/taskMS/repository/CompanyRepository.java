package com.example.taskMS.repository;

import com.example.taskMS.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    // Check if a company name is already taken during setup
    Boolean existsByName(String name);

    // Find company by name (useful for search or verification)
    Optional<Company> findByName(String name);
}