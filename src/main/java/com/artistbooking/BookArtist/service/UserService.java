package com.artistbooking.BookArtist.service;

import com.artistbooking.BookArtist.dPayload.request.UpdatePasswordRequestDto;
import com.artistbooking.BookArtist.dPayload.request.UserLoginDto;
import com.artistbooking.BookArtist.dPayload.request.UserRequestDto;
import com.artistbooking.BookArtist.dPayload.response.UserResponseDto;
import com.artistbooking.BookArtist.exception.IncorrectPasswordException;
import com.artistbooking.BookArtist.exception.PasswordMismatchException;
import com.artistbooking.BookArtist.exception.ResourceFoundException;
import com.artistbooking.BookArtist.exception.ResourceNotFoundException;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    UserResponseDto createUser(UserRequestDto userRequestDto) throws ResourceFoundException, PasswordMismatchException;

    UserResponseDto login(UserLoginDto userLoginDto, HttpServletRequest request) throws ResourceFoundException, PasswordMismatchException, ResourceNotFoundException, IncorrectPasswordException;

    String updatePassword(UpdatePasswordRequestDto updatePasswordRequestDto) throws ResourceNotFoundException, IncorrectPasswordException, PasswordMismatchException;
}
