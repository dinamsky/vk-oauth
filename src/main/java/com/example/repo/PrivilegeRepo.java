package com.example.repo;

import com.example.model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepo extends JpaRepository<Privilege,Long> {
    public Privilege findByName(String name);
}
