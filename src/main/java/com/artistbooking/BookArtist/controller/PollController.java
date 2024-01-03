package com.artistbooking.BookArtist.controller;

import com.artistbooking.BookArtist.dPayload.request.PollDto;
import com.artistbooking.BookArtist.exception.*;
import com.artistbooking.BookArtist.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/poll")
public class PollController {

    @Autowired
    private PollService pollService;


    @PostMapping(path = "/createPoll")
    public ResponseEntity<?> createPoll(@RequestBody @Valid PollDto pollDto) throws NotFoundException, RestrictedToManagerException, GeneralServiceException, ManagerNotFoundException {

        return new ResponseEntity<>(pollService.createPoll(pollDto), HttpStatus.CREATED);
    }


    @PostMapping(path = "Vote/{pollId}/{optionId}")
    public ResponseEntity<?> vote(@PathVariable Long pollId,@PathVariable Long optionId) throws NotFoundException, RestrictedToManagerException, GeneralServiceException, RestrictedAccessException, UserNotFoundException {

        return new ResponseEntity<>(pollService.vote(pollId,optionId), HttpStatus.ACCEPTED);
    }

    @DeleteMapping(path = "unvote/{pollId}")
    public ResponseEntity<?> vote(@PathVariable Long pollId) throws NotFoundException, RestrictedToManagerException, GeneralServiceException, RestrictedAccessException, UserNotFoundException {

        return new ResponseEntity<>(pollService.unvote(pollId), HttpStatus.ACCEPTED);
    }


    @GetMapping(path = "/getAllPoll")
    public ResponseEntity<?> getAllPolls() throws NotFoundException, RestrictedToManagerException, GeneralServiceException, RestrictedAccessException, UserNotFoundException, ManagerNotFoundException {

        return new ResponseEntity<>(pollService.getAllPolls(), HttpStatus.OK);
    }

    @GetMapping(path = "/getPollById/{pollId}")
    public ResponseEntity<?> getPollsById(@PathVariable Long pollId) throws NotFoundException, RestrictedToManagerException, GeneralServiceException, RestrictedAccessException {

        return new ResponseEntity<>(pollService.getPollById(pollId), HttpStatus.OK);
    }

    @PutMapping(path = "/updatePoll/{pollId}")
    public ResponseEntity<?> updatePoll(@PathVariable Long pollId,@RequestBody @Valid PollDto pollDto) throws NotFoundException, RestrictedToManagerException, GeneralServiceException, RestrictedAccessException, ManagerNotFoundException {

        return new ResponseEntity<>(pollService.updatePoll(pollId,pollDto), HttpStatus.OK);
    }


    @DeleteMapping(path = "/deletePoll/{pollId}")
    public ResponseEntity<?> deletePoll(@PathVariable Long pollId) throws NotFoundException, RestrictedToManagerException, GeneralServiceException, RestrictedAccessException, ManagerNotFoundException {

        return new ResponseEntity<>(pollService.deletePoll(pollId), HttpStatus.OK);
    }
}
