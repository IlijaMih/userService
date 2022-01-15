package com.raf.user_sevice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.raf.user_sevice.dto.ClientCreateDto;
import com.raf.user_sevice.dto.ClientEditDto;
import com.raf.user_sevice.dto.DiscountDto;
import com.raf.user_sevice.dto.IncrementReservationCountDto;
import com.raf.user_sevice.dto.ManagerCreateDto;
import com.raf.user_sevice.dto.ManagerEditDto;
import com.raf.user_sevice.dto.TokenRequestDto;
import com.raf.user_sevice.dto.TokenResponseDto;
import com.raf.user_sevice.dto.UserCreateDto;
import com.raf.user_sevice.dto.UserDto;
import com.raf.user_sevice.dto.UserEditDto;
import com.raf.user_sevice.dto.UserForbidDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<UserDto> findAll(Pageable pageable);

    UserDto add(UserCreateDto userCreateDto);
    
    UserDto addClient(ClientCreateDto userCreateDto) throws JsonProcessingException;
    
    UserDto addManager(ManagerCreateDto userCreateDto) throws JsonProcessingException;

    TokenResponseDto login(TokenRequestDto tokenRequestDto);
    
    UserDto changeAccessToUser( UserForbidDto userForbidDto);
    
    UserDto editProfile(String username, String password, UserEditDto userEditDto);
    
    UserDto editClientProfile(String username, String password, ClientEditDto userEditDto) throws JsonProcessingException;
    
    UserDto editManagerProfile(String username, String password, ManagerEditDto userEditDto) throws JsonProcessingException;
    
    void incrementReservationCountForUser(IncrementReservationCountDto dto);

    void decrementReservationCountForUser(IncrementReservationCountDto dto);
    
    DiscountDto findDiscount(Long id);


}
