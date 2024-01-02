package com.artistbooking.BookArtist.controller;

import com.artistbooking.BookArtist.exception.GeneralServiceException;
import com.artistbooking.BookArtist.exception.NotFoundException;
import com.artistbooking.BookArtist.exception.RestrictedAccessException;
import com.artistbooking.BookArtist.exception.UserNotFoundException;
import com.artistbooking.BookArtist.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/like")
public class LikeController {


    @Autowired
    private LikeService likeService;


    @PostMapping("/post/{postId}")
    public ResponseEntity<?> likePost(@PathVariable Long postId) throws RestrictedAccessException, NotFoundException, GeneralServiceException, UserNotFoundException {

        return new ResponseEntity<>(likeService.likePost(postId), HttpStatus.OK);

    }

    @DeleteMapping("/unlike-post/{postId}")
    public ResponseEntity<?> unlikePost(@PathVariable Long postId) throws RestrictedAccessException, NotFoundException, GeneralServiceException, UserNotFoundException {

        return new ResponseEntity<>(likeService.unlikePost(postId), HttpStatus.OK);

    }


    @PostMapping("/comment/{commentId}")
    public ResponseEntity<?> likeComment(@PathVariable Long commentId) throws RestrictedAccessException, NotFoundException, GeneralServiceException, UserNotFoundException {

        return new ResponseEntity<>(likeService.likeComment(commentId), HttpStatus.OK);

    }

    @DeleteMapping("/unlike-comment/{commentId}")
    public ResponseEntity<?> unlikeComment(@PathVariable Long commentId) throws RestrictedAccessException, NotFoundException, GeneralServiceException, UserNotFoundException {

        return new ResponseEntity<>(likeService.unlikeComment(commentId), HttpStatus.OK);

    }

}
