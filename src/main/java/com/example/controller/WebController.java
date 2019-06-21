package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@EnableOAuth2Client
public class WebController {
//    @Autowired
//    SecConfig config;
    @Autowired
    public AuthorizationCodeResourceDetails vk() {
        return new AuthorizationCodeResourceDetails();
    }

    @RequestMapping({ "/user", "/me" })
    public Map<String, String> user(Principal principal) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("name", principal.getName());
        return map;
    }
//TODO how to get friends fromhere
//    @RequestMapping("/friends")
//    public String friends() {
//
//        return  config.friends();
//    }
}
