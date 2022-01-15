package com.raf.user_sevice.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.raf.user_sevice.dto.ClientCreateDto;
import com.raf.user_sevice.dto.ClientEditDto;
import com.raf.user_sevice.dto.DiscountDto;
import com.raf.user_sevice.dto.ManagerCreateDto;
import com.raf.user_sevice.dto.ManagerEditDto;
import com.raf.user_sevice.dto.TokenRequestDto;
import com.raf.user_sevice.dto.TokenResponseDto;
import com.raf.user_sevice.dto.UserCreateDto;
import com.raf.user_sevice.dto.UserDto;
import com.raf.user_sevice.dto.UserEditDto;
import com.raf.user_sevice.dto.UserForbidDto;
import com.raf.user_sevice.security.CheckSecurity;
import com.raf.user_sevice.service.impl.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    
    @ApiOperation(value = "Register client")
    @PostMapping("/client")
    public ResponseEntity<UserDto> saveClient(@RequestBody @Valid ClientCreateDto userCreateDto) throws JsonProcessingException {
        return new ResponseEntity<>(userService.addClient(userCreateDto), HttpStatus.CREATED);
    }
    
    @ApiOperation(value = "Register manager")
    @PostMapping("/manager")
    public ResponseEntity<UserDto> saveManager(@RequestBody @Valid ManagerCreateDto userCreateDto) throws JsonProcessingException {
        return new ResponseEntity<>(userService.addManager(userCreateDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Login")
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> loginUser(@RequestBody @Valid TokenRequestDto tokenRequestDto) {
        return new ResponseEntity<>(userService.login(tokenRequestDto), HttpStatus.OK);
    }
    
    @ApiOperation(value = "Change user access")
    @PutMapping("/access")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<UserDto> update(@RequestHeader("Authorization") String authorization, @RequestBody @Valid UserForbidDto userForbidDto) {

        return new ResponseEntity<>(userService.changeAccessToUser(userForbidDto), HttpStatus.OK);
    }

    
    @ApiOperation(value = "Edit profile")
    @PutMapping("/client")
    @CheckSecurity(roles = {"ROLE_CLIENT"})
    public ResponseEntity<UserDto> editClientProfile(@RequestHeader("Authorization") String authorization, @RequestParam("Current username") String username, @RequestParam("Current password") String password, ClientEditDto userEditDto) throws JsonProcessingException {

        return new ResponseEntity<>(userService.editClientProfile(username,password, userEditDto), HttpStatus.OK);
    }
    
    @ApiOperation(value = "Edit profile")
    @PutMapping("/manager")
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<UserDto> editManagerProfile(@RequestHeader("Authorization") String authorization, @RequestParam("Current username") String username, @RequestParam("Current password") String password, ManagerEditDto userEditDto) throws JsonProcessingException {

        return new ResponseEntity<>(userService.editManagerProfile(username,password, userEditDto), HttpStatus.OK);
    }
    
    @GetMapping("/{id}/discount")
    public ResponseEntity<DiscountDto> getDiscount(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.findDiscount(id), HttpStatus.OK);
    }

   /* @GetMapping("/poruka")
    public String poruka() {
        return "EUREKA RADI";
    }*/


    /* @ApiOperation(value = "Get all users")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "What page number you want", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "Number of items to return", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")})
    @GetMapping
    @CheckSecurity(roles = {"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<Page<UserDto>> getAllUsers(@RequestHeader("Authorization") String authorization,
                                                     Pageable pageable) {

        return new ResponseEntity<>(userService.findAll(pageable), HttpStatus.OK);
    }*/

    /*@ApiOperation(value = "Register user")
    @PostMapping
    public ResponseEntity<UserDto> saveUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        return new ResponseEntity<>(userService.add(userCreateDto), HttpStatus.CREATED);
    }*/

    /*@PutMapping("/edit")
    public ResponseEntity<UserDto> editProfile(@RequestParam("Current username") String username, @RequestParam("Current password") String password, UserEditDto userEditDto) {

        return new ResponseEntity<>(userService.editProfile(username,password, userEditDto), HttpStatus.OK);
    }*/
}
