package com.artistbooking.BookArtist.controller;

import com.artistbooking.BookArtist.dPayload.request.PostLifeStyleRequestDto;
import com.artistbooking.BookArtist.dPayload.request.UpdatePostRequestDto;
import com.artistbooking.BookArtist.exception.NotFoundException;
import com.artistbooking.BookArtist.exception.RestrictedToManagerException;
import com.artistbooking.BookArtist.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping(path = "/post")
    public ResponseEntity<?> createPost(@RequestBody @Valid PostLifeStyleRequestDto postLifeStyleRequestDto) throws NotFoundException, RestrictedToManagerException {

        return new ResponseEntity<>(postService.postLifeStyle(postLifeStyleRequestDto), HttpStatus.CREATED);
    }


    @PutMapping(path = "/update/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id,@RequestBody UpdatePostRequestDto updatePostRequestDto) throws NotFoundException, RestrictedToManagerException {

        return new ResponseEntity<>(postService.updateLifeStylePost(id,updatePostRequestDto), HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) throws NotFoundException, RestrictedToManagerException {

        return new ResponseEntity<>(postService.deletePostLifeStyle(id), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getPost(@PathVariable Long id) throws NotFoundException {

        return new ResponseEntity<>(postService.getPostById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllPost() throws NotFoundException {

        return new ResponseEntity<>(postService.getAllPost(), HttpStatus.OK);
    }

}
