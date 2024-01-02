package com.artistbooking.BookArtist.service;

import com.artistbooking.BookArtist.dPayload.request.ArtisteDto;
import com.artistbooking.BookArtist.dPayload.response.ArtisteResponseDto;
import com.artistbooking.BookArtist.exception.*;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface ArtisteService {

  ArtisteResponseDto createArtisteProfile(ArtisteDto artisteDto, HttpSession session) throws RestrictedToManagerException, NotFoundException, DuplicateUsernameException, ManagerNotFoundException;

  ArtisteResponseDto updateArtisteProfileDto(Long artisteId, ArtisteDto artisteDto,HttpSession session) throws GeneralServiceException, NotFoundException, RestrictedToManagerException;

  ArtisteResponseDto getArtistById(Long artisteId) throws NotFoundException;

  ArtisteResponseDto getByArtisteName(String name) throws NotFoundException;

  List<ArtisteResponseDto> getAllArtiste();

}
