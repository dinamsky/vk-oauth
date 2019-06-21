//package com.example.controller;
//
//import com.example.model.Bot;
//import com.example.model.Organization;
//import com.example.repo.OrganizationRepo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//@Controller
//public class MainController {
//
//    @Autowired
//    private OrganizationRepo organizationRepository;
//
//    // @PostAuthorize("hasPermission(returnObject, 'read')")
//    @PreAuthorize("hasPermission(#id, 'Bot', 'read')")
//    @RequestMapping(method = RequestMethod.GET, value = "/foos/{id}")
//    @ResponseBody
//    public Bot findById(@PathVariable final long id) {
//        return new Bot("Sample","Test");
//    }
//
//    @PreAuthorize("hasPermission(#bot, 'write')")
//    @RequestMapping(method = RequestMethod.POST, value = "/foos")
//    @ResponseStatus(HttpStatus.CREATED)
//    @ResponseBody
//    public Bot create(@RequestBody final Bot bot) {
//        return bot;
//    }
//
//    //
//
//    @PreAuthorize("hasAuthority('BOT_READ_PRIVILEGE')")
//    @RequestMapping(method = RequestMethod.GET, value = "/foos")
//    @ResponseBody
//    public Bot findFooByName(@RequestParam final String name) {
//        return new Bot();
//    }
//
//    //
//
//    @PreAuthorize("isMember(#id)")
//    @RequestMapping(method = RequestMethod.GET, value = "/organizations/{id}")
//    @ResponseBody
//    public Organization findOrgById(@PathVariable final long id) {
//        return organizationRepository.findById(id).orElse(null);
//    }
//
//}
