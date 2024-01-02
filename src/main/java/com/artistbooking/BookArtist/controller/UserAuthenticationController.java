package com.artistbooking.BookArtist.controller;

import com.artistbooking.BookArtist.dPayload.request.UpdatePasswordRequestDto;
import com.artistbooking.BookArtist.dPayload.request.UserLoginDto;
import com.artistbooking.BookArtist.dPayload.request.UserRequestDto;
import com.artistbooking.BookArtist.exception.*;
import com.artistbooking.BookArtist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/user")
public class UserAuthenticationController {

    @Autowired
    private UserService userService;


    @Autowired
    private HttpSession httpSession;


    @PostMapping(path = "/signUp")
    public ResponseEntity<?> registerAdmin(@RequestBody @Valid UserRequestDto userRequestDto) throws ResourceFoundException, PasswordMismatchException, UserNotFoundException {

        return new ResponseEntity<>(userService.createUser(userRequestDto), HttpStatus.CREATED);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLoginDto userLoginDto, HttpServletRequest request) throws ResourceFoundException, PasswordMismatchException, IncorrectPasswordException, NotFoundException, UserNotFoundException {

        return new ResponseEntity<>(userService.login(userLoginDto,request),HttpStatus.OK);
    }

    @PutMapping(path = "/update")
    public ResponseEntity<?> updatePassword(@RequestBody @Valid UpdatePasswordRequestDto updatePasswordRequestDto) throws PasswordMismatchException, IncorrectPasswordException, NotFoundException, UserNotFoundException, RestrictedAccessException {

        return new ResponseEntity<>(userService.updatePassword(updatePasswordRequestDto),HttpStatus.ACCEPTED);

    }


    @GetMapping(path = "/logout")
    public  ResponseEntity<?> logout(HttpSession session) {

        session.invalidate();
        return ResponseEntity.ok("Logout successful");
    }
}
