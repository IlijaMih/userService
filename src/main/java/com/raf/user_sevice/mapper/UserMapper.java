package com.raf.user_sevice.mapper;


import com.raf.user_sevice.domain.Client;
import com.raf.user_sevice.domain.Manager;
import com.raf.user_sevice.domain.User;
import com.raf.user_sevice.dto.ClientCreateDto;
import com.raf.user_sevice.dto.ManagerCreateDto;
import com.raf.user_sevice.dto.UserCreateDto;
import com.raf.user_sevice.dto.UserDto;
import com.raf.user_sevice.repository.RoleRepository;
import com.raf.user_sevice.service.impl.UserService;
import com.raf.user_sevice.service.impl.UserServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private RoleRepository roleRepository;

    public UserMapper(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public UserDto userToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setUsername(user.getUsername());
        userDto.setBirthDate(user.getBirthDate());
        userDto.setPhoneNumber(user.getPhoneNumber());
        return userDto;
    }

    public User userCreateDtoToUser(UserCreateDto userCreateDto) {
        User user = new User();
        user.setEmail(userCreateDto.getEmail());
        user.setFirstName(userCreateDto.getFirstName());
        user.setLastName(userCreateDto.getLastName());
        user.setUsername(userCreateDto.getUsername());
        user.setPassword(userCreateDto.getPassword());
        user.setRole(roleRepository.findRoleByName("ROLE_USER").get());
        user.setForbiddenAccess(false);
        return user;
    }
    
    
    ////mapiranje klijenta poziva se u registraciji klijenta
    public User clientCreateDtoToUser(ClientCreateDto userCreateDto) {
    	Client client = new Client();
    	client.setEmail(userCreateDto.getEmail());
    	client.setFirstName(userCreateDto.getFirstName());
    	client.setLastName(userCreateDto.getLastName());
    	client.setUsername(userCreateDto.getUsername());
    	client.setPassword(userCreateDto.getPassword());
    	client.setRole(roleRepository.findRoleByName("ROLE_CLIENT").get());
    	client.setPassportNumber(userCreateDto.getPassportNumber());
    	client.setNumberOfReservations(0);
        client.setRank(UserServiceImpl.bronze);
    	client.setForbiddenAccess(false);
        client.setPhoneNumber(userCreateDto.getPhoneNumber());
        client.setBirthDate(userCreateDto.getBirthDate());
        User user = client;
        return user;
    }
    
    public User managerCreateDtoToUser(ManagerCreateDto userCreateDto) {
    	Manager manager = new Manager();
    	manager.setEmail(userCreateDto.getEmail());
    	manager.setFirstName(userCreateDto.getFirstName());
    	manager.setLastName(userCreateDto.getLastName());
    	manager.setUsername(userCreateDto.getUsername());
    	manager.setPassword(userCreateDto.getPassword());
    	manager.setRole(roleRepository.findRoleByName("ROLE_MANAGER").get());
    	manager.setHotelName(userCreateDto.getHotelName());
    	manager.setDateOfEmployment(userCreateDto.getDateOfEmployment());
    	manager.setForbiddenAccess(false);
        manager.setPhoneNumber(userCreateDto.getPhoneNumber());
        manager.setBirthDate(userCreateDto.getBirthDate());
        User user = manager;
        return user;
    }
    
    public User updateUser(User user) {
    	user.setForbiddenAccess(true);
    	return user;
    }
}
