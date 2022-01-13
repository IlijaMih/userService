package com.raf.user_sevice.service.impl;

import com.raf.user_sevice.repository.UserStatusRepository;
import com.raf.user_sevice.domain.UserStatus;
import com.raf.user_sevice.dto.DiscountDto;
import com.raf.user_sevice.dto.IncrementReservationCountDto;
import com.raf.user_sevice.domain.Client;
import com.raf.user_sevice.domain.Manager;
import com.raf.user_sevice.domain.User;
import com.raf.user_sevice.dto.ClientCreateDto;
import com.raf.user_sevice.dto.ClientEditDto;
import com.raf.user_sevice.dto.ManagerCreateDto;
import com.raf.user_sevice.dto.ManagerEditDto;
import com.raf.user_sevice.dto.TokenRequestDto;
import com.raf.user_sevice.dto.TokenResponseDto;
import com.raf.user_sevice.dto.UserCreateDto;
import com.raf.user_sevice.dto.UserDto;
import com.raf.user_sevice.dto.UserForbidDto;
import com.raf.user_sevice.exception.NotFoundException;
import com.raf.user_sevice.mapper.UserMapper;
import com.raf.user_sevice.repository.UserRepository;
import com.raf.user_sevice.security.service.TokenService;
import com.raf.user_sevice.dto.UserEditDto;
import com.raf.user_sevice.service.impl.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private TokenService tokenService;
    private UserRepository userRepository;
    private UserStatusRepository userStatusRepository;
    private UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, TokenService tokenService, UserMapper userMapper, UserStatusRepository userStatusRepository) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.userMapper = userMapper;
        this.userStatusRepository = userStatusRepository;
    }

    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::userToUserDto);
    }

    @Override
    public UserDto add(UserCreateDto userCreateDto) {
        User user = userMapper.userCreateDtoToUser(userCreateDto);
        userRepository.save(user);
        return userMapper.userToUserDto(user);
    }
    
    @Override
    public UserDto addClient(ClientCreateDto userCreateDto) {
        User user = userMapper.clientCreateDtoToUser(userCreateDto);
        userRepository.save(user);
        return userMapper.userToUserDto(user);
    }
    
    @Override
    public UserDto addManager(ManagerCreateDto userCreateDto) {
        User user = userMapper.managerCreateDtoToUser(userCreateDto);
        userRepository.save(user);
        return userMapper.userToUserDto(user);
    }

    @Override
    public TokenResponseDto login(TokenRequestDto tokenRequestDto) {
        //Try to find active user for specified credentials
        User user = userRepository
                .findUserByUsernameAndPassword(tokenRequestDto.getUsername(), tokenRequestDto.getPassword())
                .orElseThrow(() -> new NotFoundException(String
                        .format("User with username: %s and password: %s not found.", tokenRequestDto.getUsername(),
                                tokenRequestDto.getPassword())));
        if(user.isForbiddenAccess()) {
        	System.out.println("User is not alowed to access :)");
        	return new TokenResponseDto("User is not alowed to access :)");
        }
        //Create token payload
        Claims claims = Jwts.claims();
        claims.put("id", user.getId());
        claims.put("role", user.getRole().getName());
        //Generate token
        return new TokenResponseDto(tokenService.generate(claims));
    }

	@Override
	public UserDto changeAccessToUser( UserForbidDto userForbidDto) {
		User user = userRepository.findById(userForbidDto.getId())
				.orElseThrow(() -> new NotFoundException(String.format("User with id: %d not found.", userForbidDto.getId())));
		boolean currAccess = user.isForbiddenAccess();
		user.setForbiddenAccess(!currAccess);
		return userMapper.userToUserDto(userRepository.save(user));
	}

	@Override
	public UserDto editProfile(String username, String password, UserEditDto userEditDto) {
		User user = userRepository.findUserByUsernameAndPassword(username, password)
				.orElseThrow(() -> new NotFoundException(String.format("User with username %s and password %s not found.",userEditDto.getUsername(), userEditDto.getPassword() )));
		String newEmail = userEditDto.getEmail() == null ? user.getEmail():userEditDto.getEmail();
		user.setEmail(newEmail);
		String newFirstName = userEditDto.getFirstName() == null ? user.getFirstName():userEditDto.getFirstName();
		user.setFirstName(newFirstName);
		String newLastName = userEditDto.getLastName() == null ? user.getLastName():userEditDto.getLastName();
		user.setLastName(newLastName);
		String newPassword = userEditDto.getPassword() == null ? user.getPassword():userEditDto.getPassword();
 		user.setPassword(newPassword);
 		String newUsername = userEditDto.getUsername() == null ? user.getUsername():userEditDto.getUsername();
		user.setUsername(newUsername);
		return userMapper.userToUserDto(userRepository.save(user));
	}
	
	
	@Override
	public UserDto editClientProfile(String username, String password, ClientEditDto userEditDto) {
		User user = userRepository.findUserByUsernameAndPassword(username, password)
				.orElseThrow(() -> new NotFoundException(String.format("User with username %s and password %s not found.",userEditDto.getUsername(), userEditDto.getPassword() )));
		Client client = (Client) user;
		String newEmail = userEditDto.getEmail() == null ? client.getEmail():userEditDto.getEmail();
		client.setEmail(newEmail);
		String newFirstName = userEditDto.getFirstName() == null ? client.getFirstName():userEditDto.getFirstName();
		client.setFirstName(newFirstName);
		String newLastName = userEditDto.getLastName() == null ? client.getLastName():userEditDto.getLastName();
		client.setLastName(newLastName);
		String newPassword = userEditDto.getPassword() == null ? client.getPassword():userEditDto.getPassword();
		client.setPassword(newPassword);
 		String newUsername = userEditDto.getUsername() == null ? client.getUsername():userEditDto.getUsername();
 		client.setUsername(newUsername);
 		String newPassport = userEditDto.getPassportNumber() == null ? client.getPassportNumber():userEditDto.getPassportNumber();
 		client.setPassportNumber(newPassport);
		return userMapper.userToUserDto(userRepository.save(client));
	}
	
	@Override
	public UserDto editManagerProfile(String username, String password, ManagerEditDto userEditDto) {
		User user = userRepository.findUserByUsernameAndPassword(username, password)
				.orElseThrow(() -> new NotFoundException(String.format("User with username %s and password %s not found.",userEditDto.getUsername(), userEditDto.getPassword() )));
		Manager manager = (Manager) user;
		String newEmail = userEditDto.getEmail() == null ? manager.getEmail():userEditDto.getEmail();
		manager.setEmail(newEmail);
		String newFirstName = userEditDto.getFirstName() == null ? manager.getFirstName():userEditDto.getFirstName();
		manager.setFirstName(newFirstName);
		String newLastName = userEditDto.getLastName() == null ? manager.getLastName():userEditDto.getLastName();
		manager.setLastName(newLastName);
		String newPassword = userEditDto.getPassword() == null ? manager.getPassword():userEditDto.getPassword();
		manager.setPassword(newPassword);
 		String newUsername = userEditDto.getUsername() == null ? manager.getUsername():userEditDto.getUsername();
 		manager.setUsername(newUsername);
 		String newHotel = userEditDto.getHotelName() == null ? manager.getHotelName():userEditDto.getHotelName();
 		manager.setHotelName(newHotel);
		return userMapper.userToUserDto(userRepository.save(manager));
	}

	
   
	@Override
	public void incrementReservationCountForUser(IncrementReservationCountDto dto) {
		userRepository.findById(dto.getUserId()).ifPresent(user -> {
			((Client) user).setNumberOfReservations(((Client) user).getNumberOfReservations()+1);
			userRepository.save(user);
		});
		
	}
	
	@Override
    public DiscountDto findDiscount(Long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(String
                        .format("User with id: %d not found.", id)));
        List<UserStatus> userStatusList = userStatusRepository.findAll();
        //get discount
        Integer discount = userStatusList.stream()
                .filter(userStatus -> userStatus.getMaxNumberOfReservations() >= ((Client) user).getNumberOfReservations()
                        && userStatus.getMinNumberOfReservations() <= ((Client) user).getNumberOfReservations()).findAny().get().getDiscount();
        return new DiscountDto(discount);
    }
    
}
