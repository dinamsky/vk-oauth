package com.example.security;

import com.example.model.AppUser;
import com.example.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;

@Service
public class MyUserDetailService implements UserDetailsService {



    @Autowired
    private WebApplicationContext applicationContext;
    @Autowired
    UserRepo userRepo;
//TODO вот это нахер надо то?
//    @PostConstruct
//    public void completeSetup() {
//        userRepo = applicationContext.getBean(UserRepo.class);
//    }


    @Override
    public UserDetails loadUserByUsername(String s) {
        AppUser appUser = userRepo.findByUsername(s);
        if(appUser ==null){
            throw new UsernameNotFoundException(s);
        }

        return new MyUserPrincipal(appUser);
    }
}
