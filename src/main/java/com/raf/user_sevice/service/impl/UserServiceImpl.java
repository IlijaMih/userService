package com.raf.user_sevice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raf.user_sevice.domain.*;
import com.raf.user_sevice.dto.*;
import com.raf.user_sevice.exception.NotFoundException;
import com.raf.user_sevice.mapper.UserMapper;
import com.raf.user_sevice.repository.UserRepository;
import com.raf.user_sevice.security.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	public static final String gold ="GOLD";
	public static final String silver ="SILVER";
	public static final String bronze ="BRONZE";
    private TokenService tokenService;
    private UserRepository userRepository;
    private UserMapper userMapper;
	private JmsTemplate jmsTemplate;
	private ObjectMapper objectMapper;
	private String sendEmailDestination;
	private String changePasswordDestination;

    public UserServiceImpl(UserRepository userRepository, TokenService tokenService, UserMapper userMapper,
							JmsTemplate jmsTemplate, ObjectMapper objectMapper,@Value("${destination.send.email}") String sendEmailDestination,
						   @Value("${destination.change.password}") String changePasswordDestination ) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.userMapper = userMapper;
		this.jmsTemplate = jmsTemplate;
		this.objectMapper = objectMapper;
		this.sendEmailDestination = sendEmailDestination;
		this.changePasswordDestination = changePasswordDestination;
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
    public UserDto addClient(ClientCreateDto userCreateDto) throws JmsException,JsonProcessingException {
        User user = userMapper.clientCreateDtoToUser(userCreateDto);
        userRepository.save(user);
		jmsTemplate.convertAndSend(sendEmailDestination, objectMapper.writeValueAsString(new SendEmailDto(user.getEmail())));
        return userMapper.userToUserDto(user);
    }
    
    @Override
    public UserDto addManager(ManagerCreateDto userCreateDto) throws JmsException, JsonProcessingException {
        User user = userMapper.managerCreateDtoToUser(userCreateDto);
        userRepository.save(user);
		jmsTemplate.convertAndSend(sendEmailDestination, objectMapper.writeValueAsString(new SendEmailDto(user.getEmail())));
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
	public UserDto editClientProfile(String username, String password, ClientEditDto userEditDto) throws JsonProcessingException {
		User user = userRepository.findUserByUsernameAndPassword(username, password)
				.orElseThrow(() -> new NotFoundException(String.format("User with username %s and password %s not found.",userEditDto.getUsername(), userEditDto.getPassword() )));
		Client client = (Client) user;
		String newEmail = userEditDto.getEmail() == null ? client.getEmail():userEditDto.getEmail();
		client.setEmail(newEmail);
		String newFirstName = userEditDto.getFirstName() == null ? client.getFirstName():userEditDto.getFirstName();
		client.setFirstName(newFirstName);
		String newLastName = userEditDto.getLastName() == null ? client.getLastName():userEditDto.getLastName();
		client.setLastName(newLastName);
 		String newUsername = userEditDto.getUsername() == null ? client.getUsername():userEditDto.getUsername();
 		client.setUsername(newUsername);
 		String newPassport = userEditDto.getPassportNumber() == null ? client.getPassportNumber():userEditDto.getPassportNumber();
 		client.setPassportNumber(newPassport);
		 String newPhoneNumber = userEditDto.getPhoneNumber() == null ? client.getPhoneNumber():userEditDto.getPhoneNumber();
		 client.setPhoneNumber(newPhoneNumber);
		 String newBirthDate = userEditDto.getBirthDate() == null ? client.getBirthDate() : userEditDto.getBirthDate();
		 client.setBirthDate(newBirthDate);
		String newPassword;
		if(userEditDto.getPassword() == null){
			newPassword = client.getPassword();
		}else{
			newPassword = userEditDto.getPassword();
			jmsTemplate.convertAndSend(changePasswordDestination, objectMapper.writeValueAsString(new ChangePasswordDto(userEditDto.getPassword())));
		}
		client.setPassword(newPassword);
		return userMapper.userToUserDto(userRepository.save(client));
	}
	
	@Override
	public UserDto editManagerProfile(String username, String password, ManagerEditDto userEditDto) throws JsonProcessingException {
		User user = userRepository.findUserByUsernameAndPassword(username, password)
				.orElseThrow(() -> new NotFoundException(String.format("User with username %s and password %s not found.",userEditDto.getUsername(), userEditDto.getPassword() )));
		Manager manager = (Manager) user;
		String newEmail = userEditDto.getEmail() == null ? manager.getEmail():userEditDto.getEmail();
		manager.setEmail(newEmail);
		String newFirstName = userEditDto.getFirstName() == null ? manager.getFirstName():userEditDto.getFirstName();
		manager.setFirstName(newFirstName);
		String newLastName = userEditDto.getLastName() == null ? manager.getLastName():userEditDto.getLastName();
		manager.setLastName(newLastName);
 		String newUsername = userEditDto.getUsername() == null ? manager.getUsername():userEditDto.getUsername();
 		manager.setUsername(newUsername);
 		String newHotel = userEditDto.getHotelName() == null ? manager.getHotelName():userEditDto.getHotelName();
 		manager.setHotelName(newHotel);
		String newPhoneNumber = userEditDto.getPhoneNumber() == null ? manager.getPhoneNumber():userEditDto.getPhoneNumber();
		manager.setPhoneNumber(newPhoneNumber);
		String newBirthDate = userEditDto.getBirthDate() == null ? manager.getBirthDate() : userEditDto.getBirthDate();
		manager.setBirthDate(newBirthDate);
		String newPassword;
		if(userEditDto.getPassword() == null){
			newPassword = manager.getPassword();
		}else{
			newPassword = userEditDto.getPassword();
			jmsTemplate.convertAndSend(changePasswordDestination, objectMapper.writeValueAsString(new ChangePasswordDto(userEditDto.getPassword())));
		}
		return userMapper.userToUserDto(userRepository.save(manager));
	}

	
   
	@Override
	public void incrementReservationCountForUser(IncrementReservationCountDto dto) {
		userRepository.findById(dto.getUserId()).ifPresent(user -> {
			((Client) user).setNumberOfReservations(((Client) user).getNumberOfReservations()+1);
			setRankForUser((Client)user);
			userRepository.save(user);
		});
		
	}

	@Override
	public void decrementReservationCountForUser(IncrementReservationCountDto dto) {
		userRepository.findById(dto.getUserId()).ifPresent(user -> {
			((Client) user).setNumberOfReservations(((Client) user).getNumberOfReservations()-1);
			setRankForUser((Client)user);
			userRepository.save(user);
		});
	}

	@Override
    public DiscountDto findDiscount(Long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(String
                        .format("User with id: %d not found.", id)));

		Integer discount;
		switch (((Client)user).getRank()){
			case "GOLD":
				discount = 20;
				break;
			case "SILVER":
				discount = 10;
				break;
			case "BRONZE":
				discount = 5;
				break;
			default:
				discount = 0;
				break;
		}
        return new DiscountDto(discount);
    }

	public void setRankForUser(Client user){
		Integer numOfRes = user.getNumberOfReservations();
		if(numOfRes >= 0 && numOfRes < 10){
			user.setRank(bronze);
		}else if(numOfRes >= 10 && numOfRes < 20){
			user.setRank(silver);
		}else if(numOfRes >= 20){
			user.setRank(gold);
		}
	}
}
