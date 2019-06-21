package com.example.repo;

import com.example.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


public interface UserRepo extends JpaRepository<AppUser,Long> {
    AppUser findByUsername(final String username);
    @Transactional
    void readUserByUsername(String username);


}
