package com.artistbooking.BookArtist.controller;


import com.artistbooking.BookArtist.dPayload.request.ArtisteDto;
import com.artistbooking.BookArtist.exception.*;
import com.artistbooking.BookArtist.service.ArtisteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("api/v1/artiste")
public class ArtisteController {

    @Autowired
    private ArtisteService artisteService;


    @PostMapping("/artiste")
    public ResponseEntity<?> createArtiste(@RequestBody ArtisteDto artisteDto, HttpSession session) throws NotFoundException, RestrictedToManagerException, DuplicateUsernameException, ManagerNotFoundException {

        return new ResponseEntity<>(artisteService.createArtisteProfile(artisteDto,session), HttpStatus.CREATED);
    }

    @PutMapping("/update/{artisteId}")
    public ResponseEntity<?> updateArtiste(@RequestParam Long artisteId, @RequestBody ArtisteDto artisteDto, HttpSession session) throws NotFoundException, RestrictedToManagerException, GeneralServiceException {

        return new ResponseEntity<>(artisteService.updateArtisteProfileDto(artisteId,artisteDto,session), HttpStatus.ACCEPTED);
    }

    @GetMapping("/getId/{artisteId}")
    public ResponseEntity<?> getArtiste(@RequestParam Long artisteId) throws NotFoundException, RestrictedToManagerException, GeneralServiceException {

        return new ResponseEntity<>(artisteService.getArtistById(artisteId), HttpStatus.OK);
    }

    @GetMapping("/getName/{name}")
    public ResponseEntity<?> getArtiste(@RequestParam String name) throws NotFoundException, RestrictedToManagerException, GeneralServiceException {

        return new ResponseEntity<>(artisteService.getByArtisteName(name), HttpStatus.OK);
    }

    @GetMapping("/getAllArtiste")
    public ResponseEntity<?> getAllArtiste() throws NotFoundException {

        return new ResponseEntity<>(artisteService.getAllArtiste(), HttpStatus.OK);
    }

}
