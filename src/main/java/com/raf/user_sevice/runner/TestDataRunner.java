package com.raf.user_sevice.runner;

import com.raf.user_sevice.domain.Client;
import com.raf.user_sevice.domain.Manager;
import com.raf.user_sevice.domain.Role;
import com.raf.user_sevice.domain.User;
import com.raf.user_sevice.repository.RoleRepository;
import com.raf.user_sevice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile({"default"})
@Component
public class TestDataRunner implements CommandLineRunner {

    private RoleRepository roleRepository;
    private UserRepository userRepository;

    public TestDataRunner(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        //Insert roles
        Role roleUser = new Role("ROLE_USER", "User role");
        Role roleAdmin = new Role("ROLE_ADMIN", "Admin role");
        Role roleClient = new Role("ROLE_CLIENT", "Client role");
        Role roleManager = new Role("ROLE_MANAGER", "Manager role");
        roleRepository.save(roleUser);
        roleRepository.save(roleAdmin);
        roleRepository.save(roleClient);
        roleRepository.save(roleManager);
        
        //Insert admin
        User admin = new User();
        admin.setEmail("admin@gmail.com");
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setRole(roleAdmin);
        userRepository.save(admin);
        
        Client client = new Client();
        client.setFirstName("client");
        client.setLastName("client");
        client.setPassword("client123");
        client.setUsername("client");
        client.setEmail("client@gmail.com");
        client.setForbiddenAccess(false);
        client.setNumberOfReservations(0);
        client.setPassportNumber("1122");
        client.setRole(roleClient);
        userRepository.save(client);
        
        Manager manager = new Manager();
        manager.setDateOfEmployment("11.11.2011.");
        manager.setEmail("manager@gmail.com");
        manager.setFirstName("manager");
        manager.setForbiddenAccess(false);
        manager.setHotelName("Motel");
        manager.setLastName("manager");
        manager.setPassword("manager123");
        manager.setRole(roleManager);
        manager.setUsername("manager");
        userRepository.save(manager);
        
    }
}
