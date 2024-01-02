package com.artistbooking.BookArtist.controller;

import com.artistbooking.BookArtist.dPayload.request.*;
import com.artistbooking.BookArtist.exception.*;
import com.artistbooking.BookArtist.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/manager")
public class ManagerAuthenticationController {

    @Autowired
    private ManagerService managerService;


    @Autowired
    private HttpSession httpSession;


    @PostMapping(path = "/signUp")
    public ResponseEntity<?> registerAdmin(@RequestBody @Valid ManagerRequestDto managerRequestDto) throws ResourceFoundException, PasswordMismatchException, ManagerNotFoundException {

        return new ResponseEntity<>(managerService.createManager(managerRequestDto), HttpStatus.CREATED);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody @Valid ManagerLoginDto managerLoginDto, HttpServletRequest request) throws ResourceFoundException, PasswordMismatchException, IncorrectPasswordException, NotFoundException, ManagerNotFoundException {

        return new ResponseEntity<>(managerService.login(managerLoginDto,request),HttpStatus.OK);
    }

    @PutMapping(path = "/edit")
    public ResponseEntity<?> updatePassword(@RequestBody @Valid UpdatePasswordRequestDto updatePasswordRequestDto) throws PasswordMismatchException, IncorrectPasswordException, NotFoundException, RestrictedToManagerException, ManagerNotFoundException {

        return new ResponseEntity<>(managerService.updatePassword(updatePasswordRequestDto),HttpStatus.ACCEPTED);

    }


    @GetMapping(path = "/logout")
    public  ResponseEntity<?> logout(HttpSession session) {

        session.invalidate();
        return ResponseEntity.ok("Logout successful");
    }
}
