package com.artistbooking.BookArtist.controller;

import com.artistbooking.BookArtist.dPayload.request.CommentRequestDTO;
import com.artistbooking.BookArtist.dPayload.request.UpdateCommentRequestDto;
import com.artistbooking.BookArtist.exception.*;
import com.artistbooking.BookArtist.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("api/v1/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    private HttpSession session;

    @PostMapping("/create/{postId}")
    public ResponseEntity<?> createComment(@PathVariable Long postId, @RequestBody CommentRequestDTO commentRequestDTO,
            HttpSession session) throws RestrictedAccessException, NotFoundException {

        return new ResponseEntity<>(commentService.createComment(commentRequestDTO,postId,session),HttpStatus.OK);

    }

    @PutMapping(path = "/update/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long commentId,@RequestBody UpdateCommentRequestDto updateCommentRequestDto,
                                           HttpSession httpSession) throws NotFoundException, RestrictedToManagerException, RestrictedAccessException, GeneralServiceException {

        return new ResponseEntity<>(commentService.updateComment(commentId,updateCommentRequestDto), HttpStatus.CREATED);
    }


    @DeleteMapping(path = "/delete/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) throws NotFoundException, RestrictedToManagerException, RestrictedAccessException, GeneralServiceException {

        return new ResponseEntity<>(commentService.deleteComment(commentId), HttpStatus.OK);
    }

    @GetMapping(path = "/getByPost/{postId}")
    public ResponseEntity<?> getCommentByPost(@PathVariable Long postId) throws NotFoundException, RestrictedAccessException, UserNotFoundException, ManagerNotFoundException {

        return new ResponseEntity<>(commentService.getCommentByPostId(postId), HttpStatus.OK);
    }

    @GetMapping(path = "/getById/{commentId}")
    public ResponseEntity<?> getCommentById(@PathVariable Long commentId) throws NotFoundException,  RestrictedAccessException {

        return new ResponseEntity<>(commentService.getCommentById(commentId), HttpStatus.OK);
    }
}
