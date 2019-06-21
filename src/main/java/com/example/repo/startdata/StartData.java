package com.example.repo.startdata;

import com.example.model.AppUser;
import com.example.model.Organization;
import com.example.model.Privilege;
import com.example.repo.OrganizationRepo;
import com.example.repo.PrivilegeRepo;
import com.example.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashSet;

@Component
public class StartData {
    @Autowired
    private UserRepo userRepository;

    @Autowired
    private PrivilegeRepo privilegeRepository;

    @Autowired
    private OrganizationRepo organizationRepository;

    @Autowired
    private PasswordEncoder encoder;

    @PostConstruct
    public void init() {
        initOrganizations();
        initPrivileges();
        initUsers();
    }

    private void initUsers() {
        final Privilege privilege1 = privilegeRepository.findByName("BOT_READ_PRIVILEGE");
        final Privilege privilege2 = privilegeRepository.findByName("BOT_WRITE_PRIVILEGE");
        //
        final AppUser user1 = new AppUser();
        user1.setUsername("john");
        user1.setPassword(encoder.encode("123"));
        user1.setPrivileges(new HashSet<Privilege>(Arrays.asList(privilege1)));
        user1.setOrganization(organizationRepository.findByName("FirstOrg"));
        userRepository.save(user1);
        //
        final AppUser user2 = new AppUser();
        user2.setUsername("tom");
        user2.setPassword(encoder.encode("111"));
        user2.setPrivileges(new HashSet<Privilege>(Arrays.asList(privilege1, privilege2)));
        user2.setOrganization(organizationRepository.findByName("SecondOrg"));
        userRepository.save(user2);
    }

    private void initOrganizations() {
        final Organization org1 = new Organization("FirstOrg");
        organizationRepository.save(org1);
        //
        final Organization org2 = new Organization("SecondOrg");
        organizationRepository.save(org2);
    }

    private void initPrivileges() {
        final Privilege privilege1 = new Privilege("BOT_READ_PRIVILEGE");
        privilegeRepository.save(privilege1);
        //
        final Privilege privilege2 = new Privilege("BOT_WRITE_PRIVILEGE");
        privilegeRepository.save(privilege2);
    }
}
