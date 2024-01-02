package com.artistbooking.BookArtist.service;

import com.artistbooking.BookArtist.dPayload.request.PollDto;
import com.artistbooking.BookArtist.exception.*;

import java.util.List;

public interface PollService {

    List<PollDto> getAllPolls() throws NotFoundException, ManagerNotFoundException, UserNotFoundException;

    PollDto createPoll(PollDto pollDto) throws GeneralServiceException, NotFoundException, RestrictedToManagerException, ManagerNotFoundException;

    String vote(Long pollId, Long optionId) throws RestrictedAccessException, NotFoundException, GeneralServiceException, UserNotFoundException;

    PollDto getPollById(Long pollId) throws NotFoundException;

    PollDto updatePoll(Long pollId, PollDto pollDto) throws NotFoundException, RestrictedToManagerException, ManagerNotFoundException;

    String deletePoll(Long pollId) throws RestrictedToManagerException, NotFoundException, ManagerNotFoundException;
}
