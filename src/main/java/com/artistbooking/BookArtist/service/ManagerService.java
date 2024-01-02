package com.artistbooking.BookArtist.service;

import com.artistbooking.BookArtist.dPayload.request.ManagerLoginDto;
import com.artistbooking.BookArtist.dPayload.request.ManagerRequestDto;
import com.artistbooking.BookArtist.dPayload.request.UpdatePasswordRequestDto;
import com.artistbooking.BookArtist.dPayload.response.ManagerResponseDto;
import com.artistbooking.BookArtist.exception.*;

import javax.servlet.http.HttpServletRequest;

public interface ManagerService {

    ManagerResponseDto createManager(ManagerRequestDto managerRequestDto) throws ResourceFoundException, PasswordMismatchException, ManagerNotFoundException;

   ManagerResponseDto login(ManagerLoginDto managerLoginDto, HttpServletRequest request) throws NotFoundException, IncorrectPasswordException, ManagerNotFoundException;

    String updatePassword(UpdatePasswordRequestDto updatePasswordRequestDto) throws NotFoundException, IncorrectPasswordException, PasswordMismatchException, RestrictedToManagerException, ManagerNotFoundException;

    boolean isManager(Long managerId);

}
