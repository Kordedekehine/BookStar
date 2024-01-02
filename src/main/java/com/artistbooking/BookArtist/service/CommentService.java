package com.artistbooking.BookArtist.service;

import com.artistbooking.BookArtist.dPayload.request.CommentRequestDTO;
import com.artistbooking.BookArtist.dPayload.request.UpdateCommentRequestDto;
import com.artistbooking.BookArtist.dPayload.response.CommentResponseDTO;
import com.artistbooking.BookArtist.exception.*;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface  CommentService {

   public CommentResponseDTO createComment(CommentRequestDTO commentRequestDTO, Long postId, HttpSession httpSession) throws RestrictedAccessException, NotFoundException;
   CommentResponseDTO updateComment(Long commentId, UpdateCommentRequestDto updateCommentRequestDto) throws NotFoundException, RestrictedAccessException, GeneralServiceException;

   String deleteComment(Long commentId) throws NotFoundException, RestrictedAccessException, GeneralServiceException;

   CommentResponseDTO getCommentById(Long commentId) throws NotFoundException, RestrictedAccessException;

   List<CommentResponseDTO> getCommentByPostId(Long postId) throws NotFoundException, RestrictedAccessException, ManagerNotFoundException, UserNotFoundException;
}
