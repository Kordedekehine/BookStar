package com.artistbooking.BookArtist.service;

import com.artistbooking.BookArtist.dPayload.request.UpdatePasswordRequestDto;
import com.artistbooking.BookArtist.dPayload.request.UserLoginDto;
import com.artistbooking.BookArtist.dPayload.request.UserRequestDto;
import com.artistbooking.BookArtist.dPayload.response.UserResponseDto;
import com.artistbooking.BookArtist.exception.*;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    UserResponseDto createUser(UserRequestDto userRequestDto) throws ResourceFoundException, PasswordMismatchException, UserNotFoundException;

    UserResponseDto login(UserLoginDto userLoginDto, HttpServletRequest request) throws ResourceFoundException, PasswordMismatchException, NotFoundException, IncorrectPasswordException, UserNotFoundException;

    String updatePassword(UpdatePasswordRequestDto updatePasswordRequestDto) throws NotFoundException, IncorrectPasswordException, PasswordMismatchException, RestrictedAccessException, UserNotFoundException;
}
