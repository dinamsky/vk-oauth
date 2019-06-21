package com.example.repo;

import com.example.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepo extends JpaRepository<Organization,Long> {
    public Organization findByName(String name);
}
