package com.artistbooking.BookArtist.service;

import com.artistbooking.BookArtist.dPayload.request.ManagerLoginDto;
import com.artistbooking.BookArtist.dPayload.request.ManagerRequestDto;
import com.artistbooking.BookArtist.dPayload.request.UpdatePasswordRequestDto;
import com.artistbooking.BookArtist.dPayload.response.ManagerResponseDto;
import com.artistbooking.BookArtist.exception.IncorrectPasswordException;
import com.artistbooking.BookArtist.exception.PasswordMismatchException;
import com.artistbooking.BookArtist.exception.ResourceFoundException;
import com.artistbooking.BookArtist.exception.ResourceNotFoundException;

import javax.servlet.http.HttpServletRequest;

public interface ManagerService {

    ManagerResponseDto createManager(ManagerRequestDto managerRequestDto) throws ResourceFoundException, PasswordMismatchException;

   ManagerResponseDto login(ManagerLoginDto managerLoginDto, HttpServletRequest request) throws ResourceNotFoundException, IncorrectPasswordException;

    String updatePassword(UpdatePasswordRequestDto updatePasswordRequestDto) throws ResourceNotFoundException, IncorrectPasswordException, PasswordMismatchException;

}
